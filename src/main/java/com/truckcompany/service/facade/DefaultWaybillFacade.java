package com.truckcompany.service.facade;

import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.UserService;
import com.truckcompany.service.WaybillService;
import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.dto.WaybillDTO;
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
public class DefaultWaybillFacade implements WaybillFacade {
    private final Logger log = LoggerFactory.getLogger(DefaultWaybillFacade.class);

    @Inject
    private UserService userService;

    @Inject
    private WaybillService waybillService;


    @Override
    public List<WaybillDTO> findWaybills() {

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            log.debug("Get all waybills for user \'{}\'", user.getLogin());
            List<WaybillDTO> waybills = emptyList();
            if (isCurrentUserInRole("ROLE_DRIVER")){
                waybills = waybillService.getWaybillByDriver(user)
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }
            else if(isCurrentUserInRole("ROLE_COMPANYOWNER")){
                waybills = waybillService.getWaybillByCompany(user.getCompany())
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }
            return waybills;
        }else {
            return emptyList();
        }
    }
}
