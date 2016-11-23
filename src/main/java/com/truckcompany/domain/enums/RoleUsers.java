package com.truckcompany.domain.enums;

/**
 * Created by Vladimir on 22.11.2016.
 */
public enum RoleUsers {
    ADMIN, SUPERADMIN, DISPATCHER, MANAGER, DRIVER, COMPANYOWNER;
    public static RoleUsers getRoleUserFromString(String str){
        switch (str){
            case "ROLE_ADMIN" : return ADMIN;
            case "ROLE_SUPERADMIN": return  SUPERADMIN;
            case "ROLE_DISPATCHER": return DISPATCHER;
            case "ROLE_MANAGER" : return  MANAGER;
            case "ROLE_DRIVER" : return  DRIVER;
            default: return COMPANYOWNER;
        }
    }
}


