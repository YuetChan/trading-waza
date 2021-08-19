package com.tycorp.eb.domain;

public interface SignedInUserDetail {

    Long getUserId();
    String getUseremail();
    UserRoleEnum getUserRole();

}
