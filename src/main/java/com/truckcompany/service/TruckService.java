package com.truckcompany.service;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.RouteListRepository;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Viktor Dobroselsky.
 */
@Service
@Transactional
public class TruckService {
    private final Logger log = LoggerFactory.getLogger(TruckService.class);

    @Inject
    private TruckRepository truckRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private RouteListRepository routeListRepository;

    public Truck getTruckById (Long id) {
        Truck truck = truckRepository.getOne(id);
        log.debug("Get Information about Truck with id: {}", id);
        return truck;
    }

    public List<Truck> getAllTrucks () {
        log.debug("Get all trucks.");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        List<Truck> trucks = truckRepository.findByCompany(user.get().getCompany());

        return trucks;
    }

    public List<Truck> getAllTrucksBelongsCompany(Company company){
        log.debug("Get all trucks belongs company {}.", company);
        return truckRepository.findByCompanyIdWithCompany(company.getId());
    }

    public Page<Truck> getAllTrucksBelongsCompany(Company company, Pageable pageable){
        log.debug("Get all trucks belongs company {}.", company);
        return truckRepository.findByCompanyIdWithCompany(company.getId(), pageable);
    }

    public List<Truck> getReadyTrucksBelongsCompany(Company company){
        log.debug("Get ready trucks belongs company {}.", company);
        return truckRepository.findByCompanyAndReady(company);
    }



    public Truck getTruckByIdWIthCompany(Long id){
        log.debug("Get truck id={} with company.", id);
        return truckRepository.getOne(id);
    }

    public Truck createTruck (ManagedTruckVM managedTruckVM) {
        Truck truck = new Truck();
        setFieldsTruckFromTruckVM(truck, managedTruckVM);
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.getCompany() != null){
                truck.setCompany(user.getCompany());
            }
            truckRepository.save(truck);
        }

        log.debug("Created Information for Truck");
        return truck;
    }

    private void setFieldsTruckFromTruckVM(Truck truck, ManagedTruckVM managedTruckVM){
        truck.setRegNumber(managedTruckVM.getRegNumber());
        truck.setConsumption(managedTruckVM.getConsumption());
        truck.setType(managedTruckVM.getType());
        truck.setModel(managedTruckVM.getModel());
        truck.setStatus(managedTruckVM.getStatus());
    }

    public void updateTruck (ManagedTruckVM managedTruckVM) {
        Truck truck = truckRepository.findOne(managedTruckVM.getId());
        setFieldsTruckFromTruckVM(truck, managedTruckVM);
        truckRepository.save(truck);
        log.debug("Changed fields for Truck {}", truck);
    }

    public void deleteTruck (Long id) {
        Truck truck = truckRepository.findOne(id);
        if (truck != null) {
            truckRepository.delete(truck);
            log.debug("Deleted truck {}", id);
        }
    }

    public List<ManagedTruckVM> getFreeTrucks(Long from, Long to) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        ZonedDateTime dateFrom = ZonedDateTime.ofInstant(new Date(from).toInstant(), ZoneId.systemDefault());
        ZonedDateTime dateTo = ZonedDateTime.ofInstant(new Date(to).toInstant(), ZoneId.systemDefault());

        log.debug("\nDate from: " + dateFrom + ";\nDate to: " + dateTo);

        Set<Truck> usedTrucks = routeListRepository
            .findRouteListsByDate(user.getCompany(), dateFrom, dateTo)
            .stream()
            .map(routeList -> {
                log.info("Truck with id:" + routeList.getTruck().getId());
                log.info("Route list id:" + routeList.getId());
                return routeList.getTruck();
            }).collect(Collectors.toSet());

        List<Truck> allTrucks = truckRepository.findByCompanyAndReady(user.getCompany());
        allTrucks.removeAll(usedTrucks);

        return allTrucks.stream().map(ManagedTruckVM::new).collect(Collectors.toList());
    }
}
