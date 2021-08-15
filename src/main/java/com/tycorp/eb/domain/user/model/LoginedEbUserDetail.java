package com.tycorp.eb.domain.user.model;

public interface LoginedEbUserDetail {

    Long getUserId();
    String getUseremail();
    EbUserRoleEnum getUserRole();

}
