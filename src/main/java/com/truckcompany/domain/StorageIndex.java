package com.truckcompany.domain;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.annotation.Documented;

/**
 * Created by Vladimir on 16.11.2016.
 */

@SolrDocument(solrCoreName = "storages")
public class StorageIndex {
    @Id
    private Long id;


    @Field("name")
    private String name;


    @Field("activated")
    private boolean activated;

    @Field("deleted")
    private boolean deleted;

    @Field("address")
    private String address;

    @Field("idcompany")
    private Long idCompany;

    public StorageIndex(){}

    public StorageIndex(Long id, String name, boolean activated, boolean deleted,  String address, Long idCompany){
        this.id = id;
        this.name = name;
        this.activated = activated;
        this.deleted = deleted;
        this.address = address;
        this.idCompany = idCompany;
    }

    public StorageIndex(Storage storage, Long idCompany){
        this.id = storage.getId();
        this.name = storage.getName();
        this.activated = storage.isActivated();
        this.deleted = storage.isDeleted();
        this.address = storage.getAddress();
        this.idCompany = idCompany;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
