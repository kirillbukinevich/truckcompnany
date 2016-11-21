package com.truckcompany.service;

import com.truckcompany.domain.*;
import com.truckcompany.domain.emb_id.WaybillGoodsId;
import com.truckcompany.domain.enums.WaybillGoodsState;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.*;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.RouteListRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.repository.WaybillRepository;
import com.truckcompany.repository.WriteOffActRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedWaybillVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private WriteOffActRepository writeOffActRepository;

    @Inject
    private RouteListService routeListService;

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private RouteListRepository routeListRepository;

    public ManagedWaybillVM getWaybillById(Long id) {
        ManagedWaybillVM waybill = new ManagedWaybillVM(waybillRepository.getOne(id));
        log.debug("Get Information about Waybill with id: {}", id);
        return waybill;
    }


    @Transactional(readOnly = true)
    public List<Waybill> getWaybillForDriver() {
        log.debug("Get waybills for drivers");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        List<Waybill> waybills = waybillRepository.findByDriver(user.get());
        return waybills;
    }

    public List<WaybillDTO> getAllWaybills() {
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

    public Waybill createWaybill(ManagedWaybillVM managedWaybillVM) {
        Waybill waybill = new Waybill();

        Optional<User> dispatcher = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User driver = userRepository.findOne(managedWaybillVM.getDriverId());
        RouteList routeList = routeListService.createRouteList(managedWaybillVM.getRouteList());

        waybill.setDispatcher(dispatcher.get());
        waybill.setDriver(driver);
        waybill.setState(WaybillState.CREATED);
        waybill.setRouteList(routeList);
        waybill.setDate(ZonedDateTime.now());
        waybill.setWaybillGoods(offerRepository.getOne(managedWaybillVM.getOfferId()).getOfferGoods().stream()
            .map(og -> {
                WaybillGoods waybillGoods = new WaybillGoods();
                waybillGoods.setGoods(og.getGoods());
                waybillGoods.setCount(og.getCount());
                waybillGoods.setState(WaybillGoodsState.ACCEPTED);

                return waybillGoods;
            }).collect(Collectors.toSet()));

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

    public void updateWaybill(ManagedWaybillVM managedWaybillVM) {
        waybillRepository.findOneById(managedWaybillVM.getId()).ifPresent(w -> {
            w.setDispatcher(userRepository.findOneByLogin(managedWaybillVM.getDispatcher().getLogin()).get());
            w.setDriver(userRepository.findOneByLogin(managedWaybillVM.getDriver().getLogin()).get());
            w.setDate(managedWaybillVM.getDate());
            w.setState(managedWaybillVM.getState());
            w.setDateChecked(managedWaybillVM.getDateChecked());
            if (managedWaybillVM.getManager() != null) {
                w.setManager(userRepository.findOneById(managedWaybillVM.getManager().getId()).get());
            }
            if (managedWaybillVM.getWriteOff() != null) {
                w.setWriteOff(writeOffActRepository.getOne(managedWaybillVM.getWriteOff().getId()));
            }
            if (managedWaybillVM.getRouteList() != null) {
                w.setRouteList(routeListRepository.getOne(managedWaybillVM.getRouteList().getId()));
            }
            waybillRepository.save(w);
            log.debug("Changed fields for Waybill id={}", w.getId());
        });
    }
}
