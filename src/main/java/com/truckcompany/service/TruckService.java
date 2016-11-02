package com.truckcompany.service;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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

    public List<Truck> getTrucksBelongsCompany(Company company){
        log.debug("Get all trucks belongs company {}.", company);
        return truckRepository.findByCompanyIdWithCompany(company.getId());
    }

    public Truck getTruckByIdWIthCompany(Long id){
        log.debug("Get truck id={} with company.", id);
        return truckRepository.getOne(id);
    }

    public Truck createTruck (ManagedTruckVM managedTruckVM) {
        Truck truck = new Truck();
        truck.setRegNumber(managedTruckVM.getRegNumber());
        truck.setConsumption(managedTruckVM.getConsumption());

        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            truck.setCompany(user.getCompany());
            truckRepository.save(truck);
        }


        log.debug("Created Information for Truck");
        return truck;
    }

    public void updateTruck (ManagedTruckVM managedTruckVM) {
        Truck truck = truckRepository.findOne(managedTruckVM.getId());

        truck.setConsumption(managedTruckVM.getConsumption());
        truck.setRegNumber(managedTruckVM.getRegNumber());

        log.debug("Changed fields for Truck {}", truck);
    }

    public void deleteTruck (Long id) {
        Truck truck = truckRepository.findOne(id);
        if (truck != null) {
            truckRepository.delete(truck);
            log.debug("Deleted truck {}", id);
        }
    }
}
