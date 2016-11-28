package com.truckcompany.service;

import com.truckcompany.domain.*;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.*;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.web.rest.vm.ManagedRouteListVM;
import com.truckcompany.web.rest.vm.ManagedWaybillVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing Waybills
 * Created by Viktor Dobroselsky.
 */

@Service
@Transactional
public class WaybillService {
    private final Logger log = LoggerFactory.getLogger(WaybillService.class);

    @Inject
    private WaybillRepository waybillRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RouteListService routeListService;

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private RouteListRepository routeListRepository;

    @Inject
    private GoodsRepository goodsRepository;

    public ManagedWaybillVM getWaybillById(Long id) {
        ManagedWaybillVM waybill = new ManagedWaybillVM(waybillRepository.getOne(id));
        log.debug("Get Information about Waybill with id: {}", id);
        return waybill;
    }


    @Transactional(readOnly = true)
    public List<Waybill> getWaybillByDriver (User driver) {
        log.debug("Get waybills for driver {}", driver.getLogin());
        return waybillRepository.findByDriver(driver);
    }

    public List<Waybill> getWaybillByCompany (Company company){
        log.debug("Get waybills for company with id: {}", company.getId());
        return waybillRepository.findByCompany(company);
    }


    public Waybill getWaybillByRouteList(RouteList routeList){
        log.debug("Get waybill for routelist with id: {}", routeList.getId());
        return waybillRepository.findByRouteList(routeList).orElse(null);
    }

    public Page<Waybill> getPageWaybillByCompany(Pageable pageable, Company company){
        log.debug("Get waybills for company with id: {}", company.getId());
        return waybillRepository.findPageByCompany(company, pageable);
    }

    public Page<Waybill> getPageWaybillByDispatcher(Pageable pageable, User user) {
        log.debug("Get waybills by dispatcher with login: {}", user.getLogin());

        return  waybillRepository.findPageByDispatcher(user, pageable);
    }

    public List<WaybillDTO> getAllWaybills () {
        final List<WaybillDTO> waybillDTOList = new ArrayList<>();
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        user.ifPresent(u -> {
            List<Waybill> waybills = waybillRepository.findAll();

            waybills.stream()
                .map(WaybillDTO::new)
                .collect(Collectors.toCollection(() -> waybillDTOList));
        });

        return waybillDTOList;
    }

    public Waybill createWaybill (ManagedWaybillVM managedWaybillVM) {
        Waybill waybill = new Waybill();

        Optional<User> dispatcher = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User driver = userRepository.findOne(managedWaybillVM.getDriver().getId());
        RouteList routeList = routeListService.createRouteList(new ManagedRouteListVM(managedWaybillVM.getRouteList()));

        waybill.setDispatcher(dispatcher.get());
        waybill.setDriver(driver);
        waybill.setState(WaybillState.CREATED);
        waybill.setRouteList(routeList);
        waybill.setDate(ZonedDateTime.now());
        waybill.setCompany(dispatcher.get().getCompany());

        Offer offer = offerRepository.getOne(managedWaybillVM.getOffer().getId());
        Set<Goods> goodsSet = offer.getOfferGoods().stream()
            .map(g -> {
                Goods goods = new Goods();
                goods.setName(g.getName());
                goods.setUncheckedNumber(g.getCount());
                goods.setType(g.getType());
                goods.setState("TRANSPORTATION");

                return goods;
            }).collect(Collectors.toSet());

        waybill.setGoods(goodsSet);
        waybillRepository.save(waybill);

        log.debug("Created Information for Waybill");
        return waybill;
    }

    public void deleteWaybill(Long id) {
        Waybill waybill = waybillRepository.findOne(id);
        if (waybill != null) {
            waybillRepository.delete(waybill);
            log.debug("Deleted Waybill: {}", id);
        }
    }

    public void updateWaybill (ManagedWaybillVM managedWaybillVM) {
        waybillRepository.findOneById(managedWaybillVM.getId()).ifPresent(w -> {
            w.setDispatcher(userRepository.findOneByLogin(managedWaybillVM.getDispatcher().getLogin()).get());
            w.setDriver(userRepository.findOneByLogin(managedWaybillVM.getDriver().getLogin()).get());
            w.setDate(managedWaybillVM.getDate());
            w.setState(managedWaybillVM.getState());
            w.setDateChecked(managedWaybillVM.getDateChecked());
            if (managedWaybillVM.getManager() != null) {
                w.setManager(userRepository.findOneById(managedWaybillVM.getManager().getId()).get());
            }
            if (managedWaybillVM.getRouteList() != null) {
                w.setRouteList(routeListRepository.getOne(managedWaybillVM.getRouteList().getId()));
            }
            waybillRepository.save(w);
            log.debug("Changed fields for Waybill id={}", w.getId());
        });
    }
}
