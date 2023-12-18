package com.example.payment.enums;

public enum Role {
    ADMIN, USER;

    public static final String ADMIN_PREAUTHORIZE = "hasAuthority('ADMIN')";

    public static final String USER_PREAUTHORIZE = "hasAuthority('USER')";

}
