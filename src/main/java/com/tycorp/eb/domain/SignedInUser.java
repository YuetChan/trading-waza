package com.tycorp.eb.domain;

import lombok.AccessLevel;
import lombok.Setter;

@Setter(AccessLevel.PACKAGE)
public class SignedInUser implements SignedInUserDetail {

    private Long userId;
    private String useremail;
    private UserRoleEnum userRole;

    public SignedInUser(User user) {
        setUserId(user.getUserId());
        setUseremail(user.getUseremail());
        setUserRole(user.getUserRole());
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
