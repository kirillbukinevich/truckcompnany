package com.truckcompany.service.facade;

import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.TruckService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.web.rest.TruckResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
    public Page<TruckDTO> findTrucks(Pageable pageable, HttpServletRequest request) {

        Function<Truck, TruckDTO> toDTO = convertToTruckDto();

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!optionalUser.isPresent()) return new PageImpl<TruckDTO>(emptyList());

        User user = optionalUser.get();
        Page<Truck> pageTrucks = new PageImpl<Truck>(emptyList());
        if (isCurrentUserInRole("ROLE_ADMIN")) {
            pageTrucks = truckService.getAllTrucksBelongsCompany(user.getCompany(), pageable);
        }
        if (isCurrentUserInRole("ROLE_DISPATCHER")) {
            List<Truck> trucks = truckService.getReadyTrucksBelongsCompany(user.getCompany());
            pageTrucks = new PageImpl<Truck>(trucks, pageable, trucks.size());
        }
        return new PageImpl<TruckDTO>(pageTrucks.getContent().stream().map(toDTO).collect(toList()), pageable, pageTrucks.getTotalElements());

    }

    private Function<Truck, TruckDTO> convertToTruckDto() {
        return truck -> new TruckDTO(truck, truck.getCompany());
    }
}
