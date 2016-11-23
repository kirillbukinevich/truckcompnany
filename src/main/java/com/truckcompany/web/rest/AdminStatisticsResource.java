package com.truckcompany.web.rest;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.RoleUsers;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.UserService;
import com.truckcompany.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by Vladimir on 22.11.2016.
 */
@RestController
@RequestMapping("/api")
public class AdminStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminStatisticsResource.class);

    @Inject
    private UserService userService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private TruckRepository truckRepository;

    @RequestMapping(value = "/admin/statistic", method = RequestMethod.GET)
    public ResponseEntity getStatistics(){
        LOG.debug("REST get common statistic forn admin company");

        Optional<User> userOptional = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!userOptional.isPresent()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adminStatistic", "notfoundadmin", "Not found admin.")).body(null);
        }

        User admin = userOptional.get();


        List<User> usersBelongCompany = userRepository.findUsersBelongCompanyWithAuthorities(admin.getCompany().getId());
        getCountEacheRole(usersBelongCompany);

        List<Truck> byCompany = truckRepository.findByCompany(admin.getCompany());
        getStatisticAboutTruck(byCompany);
        return null;
    }

    private void getCountEacheRole(List<User> users){
        Map<RoleUsers, Long> countEmployeeForEachRole = new HashMap<>();
        users.stream().parallel().forEach( user ->{
            user.getAuthorities().forEach(auth -> {
                RoleUsers role = RoleUsers.getRoleUserFromString(auth.getName());
                if (countEmployeeForEachRole.containsKey(role)){
                    countEmployeeForEachRole.put(role, countEmployeeForEachRole.get(role) + 1);
                } else{
                    countEmployeeForEachRole.put(role, 1L);
                }
            });
        });

        System.out.print("");
    }

    private void getStatisticAboutTruck(List<Truck> trucks){
        Map<String, Long> statisticTruckByModel = new HashMap<>();

        trucks.stream().forEach(truck ->{
            String model =truck.getModel();
            if (statisticTruckByModel.containsKey(model)){
                statisticTruckByModel.put(model, statisticTruckByModel.get(model) +1);
            } else{
                statisticTruckByModel.put(model, 1L);
            }
        });
        System.out.print("");
    }
}
