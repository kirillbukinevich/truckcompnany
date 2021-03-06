package com.truckcompany.domain;

import com.truckcompany.repository.search.SearchableWaybillDefinition;
import com.truckcompany.web.rest.vm.ManagedWaybillVM;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Dmitry on 11.12.2016.
 */

@SolrDocument(solrCoreName = "waybills")
public class WaybillIndex implements SearchableWaybillDefinition {

    @Id
    private Long id;

    @Field(NUMBER_FIELD_NAME)
    private String number;

    @Field(COMPANY_ID_FIELD_NAME)
    private Long companyId;

    @Field(DATE_FIELD_NAME)
    private Date date;

    @Field(DRIVER_FIRST_NAME_FIELD_NAME)
    private String driverFirstName;

    @Field(DRIVER_LAST_NAME_FIELD_NAME)
    private String driverLastName;

    @Field(DISPATCHER_FIRST_NAME_FIELD_NAME)
    private String dispatcherFirstName;

    @Field(DISPATCHER_LAST_NAME_FIELD_NAME)
    private String dispatcherLastName;

    @Field(STATE_FIELD_NAME)
    private String state;

    public WaybillIndex() {}

    public WaybillIndex(ManagedWaybillVM managedWaybillVM) {
        this.id = managedWaybillVM.getId();
        this.number = managedWaybillVM.getNumber();
        this.companyId = managedWaybillVM.getDispatcher().getCompany().getId();
        this.date = new Date(managedWaybillVM.getDate().toEpochSecond());
        this.driverFirstName = managedWaybillVM.getDriver().getFirstName();
        this.driverLastName = managedWaybillVM.getDriver().getLastName();
        this.dispatcherFirstName = managedWaybillVM.getDispatcher().getFirstName();
        this.dispatcherLastName = managedWaybillVM.getDispatcher().getLastName();
        this.state = managedWaybillVM.getState().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDispatcherFirstName() {
        return dispatcherFirstName;
    }

    public void setDispatcherFirstName(String dispatcherFirstName) {
        this.dispatcherFirstName = dispatcherFirstName;
    }

    public String getDispatcherLastName() {
        return dispatcherLastName;
    }

    public void setDispatcherLastName(String dispatcherLastName) {
        this.dispatcherLastName = dispatcherLastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "WaybillIndex{" +
            "id=" + id +
            ", number='" + number + '\'' +
            ", companyId=" + companyId +
            ", state='" + state + '\'' +
            '}';
    }
}
