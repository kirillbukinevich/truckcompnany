package com.truckcompany.web.rest;

import com.truckcompany.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.MailService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.util.UploadException;
import com.truckcompany.service.util.UploadUtil;
import com.truckcompany.web.rest.vm.KeyAndPasswordVM;
import com.truckcompany.web.rest.vm.ManagedUserVM;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

import static com.truckcompany.security.SecurityUtils.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * REST controller for managing users.
 * <p>
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @param request       the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users",
        method = POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserVM);
            String baseUrl = request.getScheme() + // "http"
                "://" +                                // "://"
                request.getServerName() +              // "myhost"
                ":" +                                  // ":"
                request.getServerPort() +              // "80"
                request.getContextPath();              // "/myContextPath" or "" if deployed in root context
            mailService.sendCreationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @RequestMapping(value = "/users",
        method = PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserVM> updateUser(@RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
        }
        userService.updateUser(managedUserVM.getId(), managedUserVM.getLogin(), managedUserVM.getFirstName(),
            managedUserVM.getLastName(), managedUserVM.getEmail(), managedUserVM.isActivated(),
            managedUserVM.getLangKey(), managedUserVM.getAuthorities());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()))
            .body(new ManagedUserVM(userService.getUserWithAuthorities(managedUserVM.getId())));
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @RequestMapping(value = "/users",
        method = GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedUserVM>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAllWithAuthorities(pageable);
        List<ManagedUserVM> managedUserVMs = page.getContent().stream()
            .map(ManagedUserVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserVMs, headers, OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserVM> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
            .map(ManagedUserVM::new)
            .map(managedUserVM -> new ResponseEntity<>(managedUserVM, OK))
            .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }


    @RequestMapping(value = "/change_inital_password", method = POST)
    @ResponseBody
    public ResponseEntity<?> changeInitialAdminPassword(@RequestBody KeyAndPasswordVM keyAndPasswordVM) {
        return userService.changeInitialPasswordForAdmin(keyAndPasswordVM.getKey(), keyAndPasswordVM.getNewPassword())
            .map(user -> new ResponseEntity<>(OK))
            .orElse(new ResponseEntity<>(BAD_REQUEST));
    }

    @RequestMapping(value = "/isvalidkey/{key:.+}", method = GET)
    public ResponseEntity<?> isValidKey(@PathVariable String key){
        log.debug("User is attemping create initial password for Admin profile use key= {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> new ResponseEntity<>(OK))
            .orElse(new ResponseEntity<>(BAD_REQUEST));
    }

    @RequestMapping(value = "/user/change_status/{id}", method = GET)
    public ResponseEntity<?> changeStatusEmployee(@PathVariable Long id){
        log.debug("Change status for user with id = {}", id);
        userService.changeStatus(id);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/drivers", method = GET)
    public ResponseEntity<List<ManagedUserVM>> getDrivers () {
        log.debug("Get drivers for current company.");
        List<ManagedUserVM> userList = userService.getDrivers();

        return new ResponseEntity<>(userList, OK);
    }

    @ResponseBody
    @RequestMapping(value = "/users/uploadlogo", method = POST)
    public ResponseEntity<Void> uploadFile(@RequestParam(value = "file", required = false) String file,
                                        @RequestParam(value = "file_name") String fileName,
                                        @RequestParam(value = "user_id") Long user_id,
                                        HttpServletRequest request) throws IOException {
        try {
            String rootUploadDirectory = request.getServletContext().getRealPath("content/upload/logouser");
            String imageName = userService.uploadUserLogo(file, fileName, user_id, rootUploadDirectory);
            return new ResponseEntity<Void>(HeaderUtil.createEntityCreationAlert("image",imageName), OK);
        } catch (UploadException e) {
            log.debug("Can not upload logo for user with id = {}", user_id);
            return new ResponseEntity<Void>(HeaderUtil.createFailureAlert("logoUser", "uploadimageproblem", e.getError().toString()), BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/users/deletelogo/{user_id}", method = GET)
    public void deleteLogo(@PathVariable Long user_id, HttpServletRequest request) throws IOException {
        log.debug("REST to request to delete logo for user with id {}", user_id);
        String rootUploadDirectory = request.getServletContext().getRealPath("content/upload/logouser");
        userService.deleteUserLogo(user_id, rootUploadDirectory);
    }


    /**
     * GET /users/validlogin/:login : check the "login" User.
     *
     * @param login the validation login of the user
     * @return the ResponseEntity with status 200 (OK)  if the login isn't excited
     * or with status 400 (Bad Request) if the login is already in use,
     */
    @RequestMapping(value = "/users/validlogin/{login}", method = GET)
    public ResponseEntity<Void> checkUniqueLogin(@PathVariable String login){
        boolean present = userRepository.findOneByLogin(login.toLowerCase()).isPresent();
        return !present ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }

    /**
     * GET /users/validemail/:email : check the "email" User.
     *
     * @param email the validation email of the user
     * @return the ResponseEntity with status 200 (OK)  if the email isn't excited
     * or with status 400 (Bad Request) if the email is already in use,
     */
    @RequestMapping(value = "/users/validemail/{email:.+}", method = GET)
    public ResponseEntity<Void> checkUniqueEmail(@PathVariable String email){
        boolean present = userRepository.findOneByEmail(email.toLowerCase()).isPresent();
        return !present ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }

}
