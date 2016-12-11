package com.truckcompany.domain;

import com.truckcompany.repository.search.SearchableWaybillDefinition;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;
import java.time.ZonedDateTime;

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
    private ZonedDateTime date;

    @Field(DRIVER_FIELD_NAME)
    private String driverName;

    @Field(DISPATCHER_FIELD_NAME)
    private String dispatcherName;

    @Field(STATE_FIELD_NAME)
    private String state;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
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
            ", date=" + date +
            ", driverName='" + driverName + '\'' +
            ", dispatcherName='" + dispatcherName + '\'' +
            ", state='" + state + '\'' +
            '}';
    }
}
