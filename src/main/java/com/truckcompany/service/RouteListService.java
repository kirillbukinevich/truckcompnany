package com.truckcompany.service;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.RouteList;
import com.truckcompany.repository.RouteListRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.web.rest.vm.ManagedRouteListVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Created by Viktor Dobroselsky.
 */
@Service
@Transactional
public class RouteListService {
    private final Logger log = LoggerFactory.getLogger(RouteListService.class);

    @Inject
    private RouteListRepository routeListRepository;

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private TruckRepository truckRepository;

    public RouteList getRouteListById (Long id) {
        RouteList routeList = routeListRepository.findOne(id);
        log.debug("Get Information about RouteList with id: {}", id);
        return routeList;
    }

    public List<RouteList> getRouteListsByCompany(Company company){
        return routeListRepository.findByCompany(company).orElse(Collections.emptyList());
    }

    public Page<RouteList> getPageRouteListsByCompany(Pageable pageable, Company company){
        return routeListRepository.findPageByCompany(company, pageable);
    }

    public void deleteRouteList (Long id) {
        RouteList waybill = routeListRepository.findOne(id);
        if (waybill != null) {
            routeListRepository.delete(waybill);
            log.debug("Deleted RouteList: {}", id);
        }
    }

    public void updateRouteList (ManagedRouteListVM managedRouteListVM) {
        routeListRepository.findOneById(managedRouteListVM.getId()).ifPresent(r -> {
            r.setArrivalDate(managedRouteListVM.getArrivalDate());
            r.setLeavingDate(managedRouteListVM.getLeavingDate());
            r.setArrivalStorage(storageRepository.getOne(managedRouteListVM.getArrivalStorage().getId()));
            r.setLeavingStorage(storageRepository.getOne(managedRouteListVM.getLeavingStorage().getId()));
            r.setTruck(truckRepository.getOne(managedRouteListVM.getTruck().getId()));
        });
    }

    public RouteList createRouteList (ManagedRouteListVM managedRouteListVM) {
        RouteList routeList = new RouteList();
        routeList.setArrivalDate(managedRouteListVM.getArrivalDate());
        routeList.setLeavingStorage(storageRepository.findOne(managedRouteListVM.getLeavingStorage().getId()));
        routeList.setArrivalStorage(storageRepository.findOne(managedRouteListVM.getArrivalStorage().getId()));
        routeList.setTruck(truckRepository.findOne(managedRouteListVM.getTruck().getId()));

        routeListRepository.save(routeList);
        log.debug("Created Information for RouteList");
        return routeList;
    }
}
