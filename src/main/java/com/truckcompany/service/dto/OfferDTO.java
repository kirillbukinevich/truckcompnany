package com.truckcompany.service.dto;

import com.truckcompany.domain.Offer;
import com.truckcompany.web.rest.vm.ManagedStorageVM;

import java.time.ZonedDateTime;

public class OfferDTO {
    private Long id;

    private ZonedDateTime creationDate;

    private String createdBy;

    private String state;

    private ManagedStorageVM leavingStorage;

    private ManagedStorageVM arrivalStorage;

    public OfferDTO(Long id, ZonedDateTime creationDate, String createdBy) {
        this.id = id;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
    }

    public OfferDTO() {
    }

    public OfferDTO (Offer offer) {
        this.id = offer.getId();
        this.creationDate = offer.getCreationDate();
        this.createdBy = offer.getCreatedBy();
        this.state = offer.getState().toString();
        this.arrivalStorage = new ManagedStorageVM(offer.getArrivalStorage());
        this.leavingStorage = new ManagedStorageVM(offer.getLeavingStorage());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ManagedStorageVM getLeavingStorage() {
        return leavingStorage;
    }

    public void setLeavingStorage(ManagedStorageVM leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public ManagedStorageVM getArrivalStorage() {
        return arrivalStorage;
    }

    public void setArrivalStorage(ManagedStorageVM arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }
}
