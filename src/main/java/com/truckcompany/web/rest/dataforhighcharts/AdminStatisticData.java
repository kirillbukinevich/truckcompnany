package com.truckcompany.web.rest.dataforhighcharts;

import com.truckcompany.domain.enums.RoleUsers;
import com.truckcompany.domain.enums.TruckStatus;

import java.util.Map;

/**
 * Created by Vladimir on 26.11.2016.
 */
public class AdminStatisticData {

    int totalEmployees;
    int totalStorages;
    int totalTrucks;
    int totalBirthdayTemplate;

    Map<RoleUsers, Long> statisticEmployeeRole;
    Map<TruckStatus, Long> statisticTruckStatus;
    Map<String, Long> statisticTruckModel;


    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getTotalStorages() {
        return totalStorages;
    }

    public void setTotalStorages(int totalStorages) {
        this.totalStorages = totalStorages;
    }

    public int getTotalTrucks() {
        return totalTrucks;
    }

    public void setTotalTrucks(int totalTrucks) {
        this.totalTrucks = totalTrucks;
    }

    public int getTotalBirthdayTemplate() {
        return totalBirthdayTemplate;
    }

    public void setTotalBirthdayTemplate(int totalBirthdayTemplate) {
        this.totalBirthdayTemplate = totalBirthdayTemplate;
    }

    public Map<RoleUsers, Long> getStatisticEmployeeRole() {
        return statisticEmployeeRole;
    }

    public void setStatisticEmployeeRole(Map<RoleUsers, Long> statisticEmployeeRole) {
        this.statisticEmployeeRole = statisticEmployeeRole;
    }

    public Map<TruckStatus, Long> getStatisticTruckStatus() {
        return statisticTruckStatus;
    }

    public void setStatisticTruckStatus(Map<TruckStatus, Long> statisticTruckStatus) {
        this.statisticTruckStatus = statisticTruckStatus;
    }

    public Map<String, Long> getStatisticTruckModel() {
        return statisticTruckModel;
    }

    public void setStatisticTruckModel(Map<String, Long> statisticTruckModel) {
        this.statisticTruckModel = statisticTruckModel;
    }
}
