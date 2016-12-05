package com.truckcompany.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Vladimir on 04.12.2016.
 */
public class UserNotActivateCompanyException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public UserNotActivateCompanyException(String message) {
        super(message);
    }

    public UserNotActivateCompanyException(String message, Throwable t) {
        super(message, t);
    }
}
