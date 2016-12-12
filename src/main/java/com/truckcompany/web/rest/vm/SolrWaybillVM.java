package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.WaybillIndex;

import java.util.Date;

/**
 * Created by Dmitry on 12.12.2016.
 */
public class SolrWaybillVM {

    private Long id;
    private String number;
    private Long companyId;
    private Date date;
    private String driverFirstName;
    private String driverLastName;
    private String dispatcherFirstName;
    private String dispatcherLastName;
    private String state;

    public SolrWaybillVM(Long id, String number, Long companyId, Date date, String driverFirstName,
                         String driverLastName, String dispatcherFirstName, String dispatcherLastName, String state) {
        this.id = id;
        this.number = number;
        this.companyId = companyId;
        this.date = date;
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
        this.dispatcherFirstName = dispatcherFirstName;
        this.dispatcherLastName = dispatcherLastName;
        this.state = state;
    }

    public SolrWaybillVM(WaybillIndex index) {
        this(index.getId(), index.getNumber(), index.getCompanyId(), index.getDate(), index.getDriverFirstName(),
            index.getDriverLastName(), index.getDispatcherFirstName(), index.getDispatcherLastName(), index.getState());
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
        return "SolrWaybillVM{" +
            "id=" + id +
            ", number='" + number + '\'' +
            ", companyId=" + companyId +
            ", date=" + date +
            ", driverFirstName='" + driverFirstName + '\'' +
            ", driverLastName='" + driverLastName + '\'' +
            ", dispatcherFirstName='" + dispatcherFirstName + '\'' +
            ", dispatcherLastName='" + dispatcherLastName + '\'' +
            ", state='" + state + '\'' +
            '}';
    }
}
