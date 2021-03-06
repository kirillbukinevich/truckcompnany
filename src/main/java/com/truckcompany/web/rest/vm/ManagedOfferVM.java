package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Offer;
import com.truckcompany.service.dto.OfferDTO;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ManagedOfferVM extends OfferDTO {

    private Long companyId;

    private Long leavingStorageId;

    private Long arrivalStorageId;

    private Set<ManagedOfferGoodsVM> offerGoods = Collections.emptySet();

    public ManagedOfferVM () {

    }

    public ManagedOfferVM (Offer offer) {
        super(offer);

        this.companyId = offer.getCompany().getId();
        this.offerGoods = offer.getOfferGoods()
            .stream()
            .map(ManagedOfferGoodsVM::new)
            .collect(Collectors.toSet());
    }

    public ManagedOfferVM(Long id, ZonedDateTime creationDate, String createdBy,
                          Long companyId, Long leavingStorageId,
                          Long arrivalStorageId) {
        super(id, creationDate, createdBy);

        this.companyId = companyId;
        this.leavingStorageId = leavingStorageId;
        this.arrivalStorageId = arrivalStorageId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getLeavingStorageId() {
        return leavingStorageId;
    }

    public void setLeavingStorageId(Long leavingStorageId) {
        this.leavingStorageId = leavingStorageId;
    }

    public Long getArrivalStorageId() {
        return arrivalStorageId;
    }

    public void setArrivalStorageId(Long arrivalStorageId) {
        this.arrivalStorageId = arrivalStorageId;
    }

    public Set<ManagedOfferGoodsVM> getOfferGoods() {
        return offerGoods;
    }

    public void setOfferGoods(Set<ManagedOfferGoodsVM> offerGoods) {
        this.offerGoods = offerGoods;
    }
}
