package com.truckcompany.web.rest;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.RoleUsers;
import com.truckcompany.domain.enums.TruckStatus;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.StorageService;
import com.truckcompany.service.TemplateService;
import com.truckcompany.service.UserService;
import com.truckcompany.web.rest.dataforhighcharts.AdminStatisticData;
import com.truckcompany.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.lang.reflect.Array;
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
    @Inject
    private StorageService storageService;
    @Inject
    private TemplateService templateService;


    @RequestMapping(value = "/admin/statistic", method = RequestMethod.GET)
    public ResponseEntity getStatistics() {
        LOG.debug("REST get common statistic forn admin company");

        Optional<User> userOptional = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adminStatistic", "notfoundadmin", "Not found admin.")).body(null);
        }

        User admin = userOptional.get();
        Company company = admin.getCompany();


        AdminStatisticData statisticData = new AdminStatisticData();


        List<User> users = userRepository.findUsersBelongCompanyWithAuthorities(company.getId());
        statisticData.setTotalEmployees(users.size());
        statisticData.setStatisticEmployeeRole(getStatisticEmployeeRole(users));

        List<Truck> trucks = truckRepository.findByCompany(admin.getCompany());
        statisticData.setTotalTrucks(trucks.size());
        statisticData.setStatisticTruckStatus(getStatisticTruckStatus(trucks));
        statisticData.setStatisticTruckModel(getStatisticTruckModel(trucks));


        statisticData.setTotalStorages(storageService.getNumberStoragesBelongCompanyAndActivated(company).intValue());
        statisticData.setTotalBirthdayTemplate(templateService.getTemplatesCreatedByCurrentAdmin().size());


        return new ResponseEntity(statisticData, HttpStatus.OK);
    }

    private Map<RoleUsers, Long> getStatisticEmployeeRole(List<User> users) {
        Map<RoleUsers, Long> statiscticEmployeeRole = new HashMap<>();
        Arrays.stream(RoleUsers.values()).forEach(role -> {if (role != RoleUsers.SUPERADMIN) statiscticEmployeeRole.put(role, 0L);});
        users.stream().parallel().forEach(user -> {
            user.getAuthorities().forEach(auth -> {
                RoleUsers role = RoleUsers.getRoleUserFromString(auth.getName());
                statiscticEmployeeRole.put(role, statiscticEmployeeRole.get(role) + 1);
            });
        });
        return statiscticEmployeeRole;
    }

    private Map<TruckStatus, Long> getStatisticTruckStatus(List<Truck> trucks) {
        Map<TruckStatus, Long> statisticTruckByStatus = new HashMap();
        Arrays.stream(TruckStatus.values()).forEach(status -> statisticTruckByStatus.put(status, 0L));
        trucks.stream().parallel().forEach(truck -> {
            statisticTruckByStatus.put(truck.getStatus(), statisticTruckByStatus.get(truck.getStatus()) + 1);
        });
        return statisticTruckByStatus;
    }

    private Map<String, Long> getStatisticTruckModel(List<Truck> trucks) {
        Map<String, Long> statisticTruckByModel = new HashMap<>();

        trucks.stream().parallel().forEach(truck -> {
            String model = truck.getModel();
            if (statisticTruckByModel.containsKey(model)) {
                statisticTruckByModel.put(model, statisticTruckByModel.get(model) + 1);
            } else {
                statisticTruckByModel.put(model, 1L);
            }
        });
        return statisticTruckByModel;
    }
}
