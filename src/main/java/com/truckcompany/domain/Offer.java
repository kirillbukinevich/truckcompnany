package com.truckcompany.domain;

import com.truckcompany.domain.enums.OfferState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table  (name = "offer")
public class Offer extends AbstractCreationInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;

    @JoinColumn(name = "company_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JoinColumn(name = "leaving_storage_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Storage leavingStorage;

    @JoinColumn(name = "arrival_storage_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Storage arrivalStorage;

    @JoinColumn (name = "offer_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OfferGoods> offerGoods;

    @Column (name = "state")
    @Enumerated(value = EnumType.STRING)
    private OfferState state;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Storage getLeavingStorage() {
        return leavingStorage;
    }

    public void setLeavingStorage(Storage leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public Storage getArrivalStorage() {
        return arrivalStorage;
    }

    public void setArrivalStorage(Storage arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }

    public Set<OfferGoods> getOfferGoods() {
        return offerGoods;
    }

    public void setOfferGoods(Set<OfferGoods> offerGoods) {
        this.offerGoods = offerGoods;
    }

    public OfferState getState() {
        return state;
    }

    public void setState(OfferState state) {
        this.state = state;
    }
}
