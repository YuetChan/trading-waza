package com.tycorp.eb.domain.user.model;

public class LoginedEbUser implements LoginedEbUserDetail {

    private Long userId;
    private String useremail;
    private EbUserRoleEnum userRole;


    public LoginedEbUser(EbUser user) {
        userId = user.getUserId();
        useremail = user.getUseremail();
        userRole = user.getUserRole();
    }


    @Override
    public Long getUserId() {
        return userId;
    }
    @Override
    public String getUseremail() {
        return useremail;
    }
    @Override
    public EbUserRoleEnum getUserRole() {
        return userRole;
    }

}
