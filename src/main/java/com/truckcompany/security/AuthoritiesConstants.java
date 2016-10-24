package com.truckcompany.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String SUPERADMIN = "ROLE_SUPERADMIN";

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String DISPATCHER = "ROLE_DISPATCHER";

    public static final String MANAGER = "ROLE_MANAGER";

    public static final String DRIVER = "ROLE_DRIVER";

    public static final String COMPANYOWNER = "ROLE_COMPANYOWNER";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
