package com.truckcompany.service.facade;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.TruckStatus;
import com.truckcompany.repository.RouteListRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.TruckService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.web.rest.TruckResource;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
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
    @Inject
    RouteListRepository routeListRepository;

    @Override
    public Page<TruckDTO> findTrucks(Pageable pageable, HttpServletRequest request) {

        Function<Truck, TruckDTO> toDTO = convertToTruckDto();

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!optionalUser.isPresent()) return new PageImpl<TruckDTO>(emptyList());

        User user = optionalUser.get();
        if (user.getCompany() == null) return new PageImpl<TruckDTO>(emptyList());
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

    public ManagedTruckVM getTruckById(Long id) {
        Truck truck = truckService.getTruckByIdWIthCompany(id);

        if (truck != null) {
            ManagedTruckVM managedTruckVM = new ManagedTruckVM(truck);
            ZonedDateTime now = ZonedDateTime.now();

            Optional<List<RouteList>> routeListsByTruck = routeListRepository.findRouteListsByTruck(truck);
            if (routeListsByTruck.isPresent()) {
                List<RouteList> routeLists = routeListsByTruck.get();
                for (RouteList routeList : routeLists) {
                    ZonedDateTime arrivalDate = routeList.getArrivalDate();
                    ZonedDateTime leavingDate = routeList.getLeavingDate();
                    if ((leavingDate.compareTo(now) <= 0) && (arrivalDate.compareTo(now) >= 0)) {
                        managedTruckVM.setBusyFrom(leavingDate);
                        managedTruckVM.setBusyTo(arrivalDate);
                        managedTruckVM.setStatus(TruckStatus.BUSY);
                    }
                }
            }
            return managedTruckVM;
        } else return null;
    }

    private Function<Truck, TruckDTO> convertToTruckDto() {
        return truck -> new TruckDTO(truck, truck.getCompany());
    }
}
