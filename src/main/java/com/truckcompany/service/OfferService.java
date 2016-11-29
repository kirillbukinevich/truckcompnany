package com.truckcompany.service;

import com.truckcompany.domain.*;
import com.truckcompany.domain.enums.OfferState;
import com.truckcompany.repository.*;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.dto.OfferDTO;
import com.truckcompany.web.rest.vm.ManagedOfferVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Viktor Dobroselsky on 02.11.2016.
 */

@Service
@Transactional
public class OfferService {

    private final Logger log = LoggerFactory.getLogger(OfferService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private OfferRepository offerRepository;

    @Transactional
    public Offer createOffer (ManagedOfferVM managedOfferVM){
        final Offer offer = new Offer();

        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .ifPresent(user->{
                offer.setLeavingStorage(storageRepository.getOne(managedOfferVM.getLeavingStorageId()));
                offer.setArrivalStorage(storageRepository.getOne(managedOfferVM.getArrivalStorageId()));
                offer.setCompany(companyRepository.getOne(managedOfferVM.getCompanyId()));
                offer.setState(OfferState.NEW);
                offer.setOfferGoods(managedOfferVM.getOfferGoods()
                    .stream()
                    .map( o -> {
                        OfferGoods offerGoods = new OfferGoods();

                        offerGoods.setName(o.getName());
                        offerGoods.setCount(o.getCount());
                        offerGoods.setType(o.getType());

                        return  offerGoods;
                    }).collect(Collectors.toSet()));

                offerRepository.save(offer);
            });

        log.debug("Created Information for Offer: {}", offer);

        return offer;
    }

    public Page<OfferDTO> getAllOffers (Pageable pageable) {
        log.debug("Get all offers.");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        Page<OfferDTO> offers = offerRepository.findByCompany(user.get().getCompany(), pageable)
            .map(OfferDTO::new);

        return offers;
    }

    public ManagedOfferVM getOfferById (Long id) {
        ManagedOfferVM offer = new ManagedOfferVM(offerRepository.getOne(id));

        return offer;
    }

    public boolean updateOfferState(ManagedOfferVM managedOfferVM) {
        Offer offer = offerRepository.getOne(managedOfferVM.getId());
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if (user.getCompany() == offer.getCompany()) {
            offer.setState(OfferState.valueOf(managedOfferVM.getState()));
            offerRepository.save(offer);

            return true;
        } else
            return false;
    }

    public OfferDTO generateOffer () {
        Offer offer = new Offer();
        Random random = new Random();
        User dispatcher = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        List<Storage> storages = storageRepository.findByCompany(dispatcher.getCompany());

        offer.setState(OfferState.NEW);
        offer.setCompany(dispatcher.getCompany());
        offer.setArrivalStorage(storages.get(random.nextInt(storages.size())));
        offer.setLeavingStorage(storages.get(random.nextInt(storages.size())));

        Set<OfferGoods> goodsSet = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            OfferGoods goods = new OfferGoods();
            goods.setName("Item_" + (i + 1));
            goods.setCount((long)random.nextInt(50));
            goods.setType("unit");

            goodsSet.add(goods);
        }

        offer.setOfferGoods(goodsSet);

        offer = offerRepository.save(offer);

        return new OfferDTO(offer);
    }
}
