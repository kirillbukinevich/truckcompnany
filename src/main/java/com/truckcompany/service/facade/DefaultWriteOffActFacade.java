package com.truckcompany.service.facade;

import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.UserService;
import com.truckcompany.service.WriteOffActService;
import com.truckcompany.service.dto.WriteOffActDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;

@Component
@Transactional
public class DefaultWriteOffActFacade implements WriteOffActFacade {
    private final Logger log = LoggerFactory.getLogger(DefaultWriteOffActFacade.class);

    @Inject
    private UserService userService;

    @Inject
    private WriteOffActService writeOffActService;


    @Override
    public List<WriteOffActDTO> findWriteOffActs() {
        Optional<User> optionalUser = userService
            .getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            log.debug("Get all writeOffActs for user \'{}\'", user.getLogin());
            List<WriteOffActDTO> writeOffActs = emptyList();
            if(isCurrentUserInRole("ROLE_COMPANYOWNER")){
                writeOffActs = writeOffActService.getWriteOffActByCompany(user.getCompany())
                    .stream()
                    .map(WriteOffActDTO::new)
                    .collect(Collectors.toList());
            }
            return writeOffActs;
        }else {
            return emptyList();
        }
    }
}
