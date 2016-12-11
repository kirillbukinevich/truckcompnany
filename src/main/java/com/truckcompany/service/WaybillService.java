package com.truckcompany.service;

import com.truckcompany.domain.*;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.OfferRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.repository.WaybillRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.web.rest.vm.ManagedRouteListVM;
import com.truckcompany.web.rest.vm.ManagedWaybillVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
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

    public ManagedWaybillVM getWaybillById(Long id) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Waybill waybill = waybillRepository.getOne(id);

        log.debug("Get Information about Waybill with id: {}", id);

        if (user.getCompany().equals(waybill.getCompany()))
            return new ManagedWaybillVM(waybill);
        else
            return null;
    }


    @Transactional(readOnly = true)
    public List<Waybill> getWaybillByDriver(User driver) {
        log.debug("Get waybills for driver {}", driver.getLogin());
        return waybillRepository.findByDriver(driver);
    }

    public List<Waybill> getWaybillByCompany(Company company) {
        log.debug("Get waybills for company with id: {}", company.getId());
        return waybillRepository.findByCompany(company);
    }

    public Page<Waybill> getPageWaybillByCompanyAndWithStolenGoods(Pageable pageable, Company company) {
        log.debug("Get waybills with stolen goods and company with id: {}", company.getId());
        List<Waybill> waybills = waybillRepository.findByCompanyAndState(company, WaybillState.DELIVERED);
        int total = waybills.size();
        waybills = waybills
            .stream()
            .filter(waybill -> waybill.getGoods()
                .stream()
                .anyMatch(
                    goods -> goods.getDeliveredNumber() != null &&
                        !Objects.equals(goods.getDeliveredNumber(), goods.getAcceptedNumber())
                )
            )
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .collect(Collectors.toList());

        return new PageImpl<>(waybills, pageable, total);
    }

    public List<WaybillDTO> getWaybillsByCompanyAndWithStolenGoods(Company company, ZonedDateTime fromDate,
                                                                      ZonedDateTime toDate) {
        log.debug("Get waybills with stolen goods and company with id: {}", company.getId());
        List<WaybillDTO> waybills = waybillRepository.findByCompanyAndStateAndDateBetween(company, WaybillState.DELIVERED,
            fromDate, toDate)
            .stream()
            .filter(waybill -> waybill.getGoods()
                .stream()
                .anyMatch(
                    goods -> !Objects.equals(goods.getDeliveredNumber(), goods.getAcceptedNumber())
                )
            )
            .map(WaybillDTO::new)
            .collect(Collectors.toList());

        return waybills;
    }


    public List<Waybill> getWaybillByCompanyAndRouteListCreationDateBetween(Company company, ZonedDateTime fromDate,
                                                                            ZonedDateTime toDate) {
        log.debug("Get waybill for company with id: {}", company.getId());
        return waybillRepository.findByCompanyAndRouteListCreationDateBetween(company, fromDate, toDate);
    }

    public List<Waybill> getWaybillByCompanyAndState(Company company, WaybillState state) {
        log.debug("Get waybill with state {} for company with id: {}", state.toString(), company.getId());
        return waybillRepository.findByCompanyAndState(company, state);
    }

    public List<Waybill> getWaybillByCompanyAndStateAndDateBetween(Company company, WaybillState state,
                                                                   ZonedDateTime fromDate, ZonedDateTime toDate) {
        log.debug("Get waybill with state {}, date between {} and {} for company with id: {}", state.toString(),
            fromDate, toDate, company.getId());
        return waybillRepository.findByCompanyAndStateAndDateBetween(company, state, fromDate, toDate);
    }

    public Page<Waybill> getPageWaybillByCompany(Pageable pageable, Company company) {
        log.debug("Get waybills for company with id: {}", company.getId());
        return waybillRepository.findPageByCompany(company, pageable);
    }

    public Page<Waybill> getPageWaybillByDispatcher(Pageable pageable, User user) {
        log.debug("Get waybills by dispatcher with login: {}", user.getLogin());

        return waybillRepository.findPageByDispatcher(user, pageable);
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
                goods.setUncheckedNumber((long)g.getCount());
                goods.setType(g.getType());
                goods.setPrice(g.getPrice());
                goods.setState("UNCHECKED");

                return goods;
            }).collect(Collectors.toSet());

        waybill.setGoods(goodsSet);
        Waybill createdWaybill = waybillRepository.save(waybill);

        createdWaybill.setNumber(this.generateNumber(dispatcher.get().getCompany().getId(), createdWaybill.getDate(), createdWaybill.getId()));
        waybillRepository.save(createdWaybill);

        log.debug("Waybill created successfully!");
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
            w.setMargin(managedWaybillVM.getMargin());

            if (managedWaybillVM.getManager() != null) {
                w.setManager(userRepository.findOneById(managedWaybillVM.getManager().getId()).get());
            }
            if (managedWaybillVM.getRouteList() != null) {
                w.getRouteList().setArrivalDate(managedWaybillVM.getRouteList().getArrivalDate());
                w.getRouteList().setCreationDate(managedWaybillVM.getRouteList().getCreationDate());
                w.getRouteList().setDistance(managedWaybillVM.getRouteList().getDistance());
                w.getRouteList().setFuelCost(managedWaybillVM.getRouteList().getFuelCost());
                w.getRouteList().setState(managedWaybillVM.getRouteList().getState());
                w.getRouteList().getArrivalStorage().setAddress(managedWaybillVM.getRouteList().getArrivalStorage().getAddress());
                w.getRouteList().getLeavingStorage().setAddress(managedWaybillVM.getRouteList().getLeavingStorage().getAddress());
            }
            waybillRepository.save(w);
            log.debug("Changed fields for Waybill id={}", w.getId());
        });
    }

    public String generateNumber(Long companyId, ZonedDateTime date, Long waybillId) {
        String companyNum = String.format("%02d", companyId);
        String dateStr = Integer.toString(date.getYear()).substring(2) + String.format("%02d", date.getMonthValue()) + String.format("%02d", date.getDayOfMonth());
        String waybillNum = String.format("%02d", waybillId);

        return "W" + companyNum + dateStr + waybillNum;
    }

    public Page<Waybill> getPageWaybillsByCompanyAndRouteListCreationDateBetween(Pageable pageable, Company company, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return waybillRepository.findPageByCompanyAndRouteListCreationDateBetween(company, fromDate, toDate, pageable);
    }


}
