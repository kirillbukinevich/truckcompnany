package com.truckcompany.service;

import com.truckcompany.domain.*;
import com.truckcompany.domain.emb_id.WaybillGoodsId;
import com.truckcompany.domain.enums.WaybillGoodsState;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.*;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
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

    public ManagedWaybillVM getWaybillById(Long id) {
        ManagedWaybillVM waybill = new ManagedWaybillVM(waybillRepository.getOne(id));
        log.debug("Get Information about Waybill with id: {}", id);
        return waybill;
    }

    public List<ManagedWaybillVM> getAllWaybills () {
        final List<ManagedWaybillVM> managedWaybillVMs = new ArrayList<>();
        Optional <User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        user.ifPresent( u -> {
            List<Waybill> waybills = waybillRepository.findAll();

            waybills.stream()
                .map(ManagedWaybillVM::new)
                .collect(Collectors.toCollection(() -> managedWaybillVMs));
        });

        return managedWaybillVMs;
    }

    public Waybill createWaybill (ManagedWaybillVM managedWaybillVM) {
        Waybill waybill = new Waybill();

        Optional<User> dispatcher = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User driver = userRepository.findOne(managedWaybillVM.getDriver().getId());
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
}
