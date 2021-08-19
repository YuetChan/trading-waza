package com.tycorp.eb.domain;

public class SignedInUser implements SignedInUserDetail {

    private Long userId;
    private String useremail;
    private UserRoleEnum userRole;

    public SignedInUser(User user) {
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
    public UserRoleEnum getUserRole() {
        return userRole;
    }

}
