package com.truckcompany.service.facade;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.RouteListService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.service.dto.TruckDTO;
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
public class DefaultRouteListFacade implements RouteListFacade {
    private final Logger log = LoggerFactory.getLogger(DefaultRouteListFacade.class);

    @Inject
    private RouteListService routeListService;

    @Inject
    private UserService userService;


    @Override
    public List<RouteListDTO> findRouteLists() {

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            log.debug("Get all routeLists for user \'{}\'", user.getLogin());
            List<RouteListDTO> routeLists = emptyList();
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")){
                routeLists = routeListService.getRouteListsByCompany(user.getCompany())
                                .stream()
                                .map(s -> toCompanyOwnerDTO(s))
                                .collect(Collectors.toList());
            }
            return routeLists;
        }else {
            return emptyList();
        }
    }

    private RouteListDTO toCompanyOwnerDTO(RouteList routeList){
        RouteListDTO baseDTO = new RouteListDTO(routeList.getId(), routeList.getDate(),
            routeList.getLeavingDate(), routeList.getArrivalDate());
        baseDTO.setLeavingStorage(new StorageDTO(routeList.getLeavingStorage()));
        baseDTO.setArrivalStorage(new StorageDTO(routeList.getArrivalStorage()));
        baseDTO.setTruck(new TruckDTO(routeList.getTruck()));
        baseDTO.setWaybill( routeList.getWaybill() != null ?
            new WaybillDTO(routeList.getWaybill().getId(), routeList.getWaybill().getDate(),
                routeList.getWaybill().getState().toString()) : null);
        return baseDTO;
    }

}
