package com.truckcompany.service.facade;

import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.TruckService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.service.dto.TruckDTO;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by Vladimir on 02.11.2016.
 */
@Component
public class DefaultTruckFacade implements TruckFacade {
    @Inject
    UserService userService;
    @Inject
    TruckService truckService;


    @Override
    public List<TruckDTO> findTrucks() {

        Function<Truck, TruckDTO> toDTO = convertToTruckDto();

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Truck> trucks = emptyList();
            if (isCurrentUserInRole("ROLE_ADMIN") || isCurrentUserInRole("ROLE_DISPATCHER")) {
                trucks = truckService.getTrucksBelongsCompany(user.getCompany());
            }
            return trucks.stream().map(toDTO).collect(toList());
        } else{
            return emptyList();
        }
    }

    private Function<Truck, TruckDTO> convertToTruckDto(){
        return truck -> new TruckDTO(truck, truck.getCompany());
    }
}
