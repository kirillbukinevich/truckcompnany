package com.truckcompany.service.dto;

import com.truckcompany.domain.Offer;

import java.time.ZonedDateTime;

public class OfferDTO {
    private Long id;

    private ZonedDateTime creationDate;

    private String createdBy;

    private String state;

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
}
