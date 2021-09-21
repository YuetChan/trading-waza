package com.tycorp.tw.domain;

public interface SignedInUserDetail {

    Long getUserId();
    String getUseremail();
    UserRoleEnum getUserRole();

}
