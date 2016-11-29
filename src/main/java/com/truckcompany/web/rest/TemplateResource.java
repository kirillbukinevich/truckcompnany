package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.MailError;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import com.truckcompany.repository.MailErrorRepository;
import com.truckcompany.repository.TemplateRepository;
import com.truckcompany.service.TemplateService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.service.util.UploadException;
import com.truckcompany.service.util.UploadUtil;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.util.PaginationUtil;
import com.truckcompany.web.rest.vm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.HeaderUtil.createAlert;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Vladimir on 07.11.2016.
 */
@RestController
@RequestMapping("/api")
public class TemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateResource.class);

    @Inject
    private TemplateService templateService;

    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private UserService userService;


    @Inject
    private MailErrorRepository mailErrorRepository;

    @RequestMapping(value = "/templates/errors", method = GET)
    public ResponseEntity<List<ManagedMailErrorVM>> getAllErrors() {
        List<MailError> mailErrors = mailErrorRepository.findAllWithTemplateAndRecipients();
        List<ManagedMailErrorVM> managedMailErrorVMs = mailErrors.stream()
            .map(mailError -> new ManagedMailErrorVM(mailError, mailError.getTemplate()))
            .collect(toList());
        return new ResponseEntity(managedMailErrorVMs, OK);
    }

    @RequestMapping(value = "/templates/errors/{errorId}", method = DELETE)
    public ResponseEntity<Void> deleteError(@PathVariable Long errorId) {
        try {
            mailErrorRepository.delete(errorId);
            return new ResponseEntity(OK);
        } catch(EmptyResultDataAccessException ex){
            return new ResponseEntity(BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/templates/sendagain/{errorId}", method = GET)
    public ResponseEntity sendEmailAgain(@PathVariable Long errorId) {
        HttpStatus status = templateService.sendBirthdayCardAgain(errorId) ? OK : BAD_REQUEST;
        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/templates/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedTemplateVM> getTemplate(@PathVariable Long id) {
        LOG.debug("REST request to get Template with id {}", id);

        Template template = templateService.getTemplate(id);
        HttpStatus status = template == null ? NOT_FOUND : OK;
        ManagedTemplateVM body = template == null ? null : new ManagedTemplateVM(template, template.getRecipient(), null);
        return new ResponseEntity<ManagedTemplateVM>(body, status);
    }


    @RequestMapping(value = "/templates", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedTemplateVM>> getTemplates(Pageable pageable) throws URISyntaxException {
        LOG.debug("REST request to get Template");


        Page<Template> page = templateService.getTemplatesCreatedByCurrentAdmin(pageable);

        List<ManagedTemplateVM> managedTemplateVMs = page.getContent().stream()
            .map(template -> new ManagedTemplateVM(template, template.getRecipient(), template.getAdmin()))
            .collect(Collectors.toList());


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/templates");
        return new ResponseEntity<>(managedTemplateVMs, headers, OK);

    }

    @RequestMapping(value = "/templates", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTemplate(@RequestBody ManagedTemplateVM template) throws URISyntaxException {
        LOG.debug("REST request to save Template: {};", template.getName());

        Template result = templateService.createTemplate(template);

        return ResponseEntity.created(new URI("/templates/" + result.getId()))
            .headers(createAlert("template.created", valueOf(result.getId())))
            .body(new ManagedTemplateVM(result));
    }

    @RequestMapping(value = "/templates", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTemplate(@RequestBody ManagedTemplateVM template) throws URISyntaxException {
        LOG.debug("REST request to update Template with id: {};", template.getId());

        Template result = templateService.updateTemplate(template);

        return ResponseEntity.created(new URI("/templates/" + result.getId()))
            .headers(createAlert("template.update", valueOf(result.getId())))
            .body(new ManagedTemplateVM(result));
    }

    @RequestMapping(value = "/templates/{templateId}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long templateId) {
        LOG.debug("REST request to delete Template: {}", templateId);
        templateRepository.delete(templateId);
        return ResponseEntity.ok().headers(createAlert("template.deleted", valueOf(templateId))).build();
    }

    @RequestMapping(value = "/templates/deleteArray", method = POST)
    public ResponseEntity<Void> deleteCompanies(@RequestBody Long[] idList) {
        LOG.debug("REST request to delete list of companies {}", idList);
        templateService.deleteTemplates(idList);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("templates.deleted", "null")).build();
    }

    @ResponseBody
    @RequestMapping(value = "/template/upload", method = POST)
    public String testSendEmail(@RequestParam(value = "upload", required = false) Part file, HttpServletRequest request) throws IOException {
        LOG.debug("Upload image for template");
        String rootUploadDirectory = request.getServletContext().getRealPath("content/upload/templateimage");
        try {
            String name = UploadUtil.uploadImage(file, rootUploadDirectory);
            String rootURL = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
            String fullPath = rootURL + "/content/upload/templateimage/" + name;
            String result = String.format("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(\"%s\", \"%s\", \"%s\" );</script>",
                request.getParameter("CKEditorFuncNum"), fullPath, "File is uploaded.");
            return result;
        } catch (UploadException e) {
            LOG.debug("Problem with upload image for mail template", e);
            return null;
        }
    }


    @RequestMapping(value = "/template/employee", method = GET)
    public ResponseEntity<List<ManagedUserVM>> getEmployeeWithoutAssignedTemplate() {
        LOG.debug("REST request to get Employees(Users) without assigned template for birthday card ");
        List<User> users = userService.getUsersBelongCompanyWithoutAssignedTemplate();
        List<ManagedUserVM> managedUserVMs = new ArrayList<>();
        for (User user : users) {
            managedUserVMs.add(new ManagedUserVM(user));
        }
        HttpHeaders headers = createAlert("templates.employee.getAll", null);
        return new ResponseEntity<>(managedUserVMs, headers, OK);
    }

    @RequestMapping(value = "/template/employee/{idUserInclude}", method = GET)
    public ResponseEntity<List<ManagedUserVM>> getEmployeeWithoutAssignedTemplateIncludingUser(@PathVariable Long idUserInclude) {
        LOG.debug("REST request to get Employees(Users) without assigned template for birthday card includind current user ");
        List<User> users = userService.getUsersBelongCompanyWithoutAssignedTemplateIncludeUserById(idUserInclude);

        List<ManagedUserVM> managedUserVMs = new ArrayList<>();
        for (User user : users) {
            managedUserVMs.add(new ManagedUserVM(user));
        }
        HttpHeaders headers = createAlert("templates.employee.getAll", null);
        return new ResponseEntity<>(managedUserVMs, headers, OK);
    }
}
