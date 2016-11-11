package com.truckcompany.service;

import com.truckcompany.domain.Offer;
import com.truckcompany.domain.OfferGoods;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.OfferState;
import com.truckcompany.repository.*;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedOfferVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
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

    @Inject
    private OfferGoodsRepository offerGoodsRepository;

    @Transactional
    public Offer createOffer (ManagedOfferVM managedOfferVM){
        final Offer offer = new Offer();

        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .ifPresent(user->{
                offer.setLeavingStorage(storageRepository.getOne(managedOfferVM.getLeavingStorageId()));
                offer.setArrivalStorage(storageRepository.getOne(managedOfferVM.getArrivalStorageId()));
                offer.setCompany(companyRepository.getOne(managedOfferVM.getCompanyId()));
                offer.setState(OfferState.valueOf(managedOfferVM.getState()));
                offer.setOfferGoods(managedOfferVM.getOfferGoods()
                    .stream()
                    .map( o -> {
                        OfferGoods offerGoods = new OfferGoods();

                        offerGoods.setName(o.getName());
                        offerGoods.setCount(o.getCount());

                        return  offerGoods;
                    }).collect(Collectors.toSet()));

                offerRepository.save(offer);
            });

        log.debug("Created Information for Offer: {}", offer);

        return offer;
    }

    public List<ManagedOfferVM> getAllOffers () {
        log.debug("Get all offers.");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        List<ManagedOfferVM> offers = offerRepository.findByCompany(user.get().getCompany())
            .stream()
            .map(ManagedOfferVM::new)
            .collect(Collectors.toList());

        return offers;
    }

    public ManagedOfferVM getOfferById (Long id) {
        ManagedOfferVM offer = new ManagedOfferVM(offerRepository.getOne(id));

        return offer;
    }

    public boolean setCancelState (Long id) {
        Offer offer = offerRepository.getOne(id);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if (user.getCompany() == offer.getCompany()) {
            offer.setState(OfferState.CANCELED);
            offerRepository.save(offer);

            return true;
        } else
            return false;
    }
}
