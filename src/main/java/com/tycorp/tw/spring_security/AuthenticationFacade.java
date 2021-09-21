package com.tycorp.tw.spring_security;

import com.tycorp.tw.domain.User;
import com.tycorp.tw.domain.UserRoleEnum;
import com.tycorp.tw.domain.SignedInUser;
import com.tycorp.tw.domain.SignedInUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

    public SignedInUserDetail getAuthenticatedUserDetail() {
        return (SignedInUserDetail)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public SignedInUserDetail getDefaultAuthenticatedUserDetail() {
        User defaultUser = new User(0l, "defaultUser@tradingwaza.com", UserRoleEnum.ADMIN);
        return new SignedInUser(defaultUser);
    }

    public SignedInUserDetail getDefaultAuthenticatedUserDetail_test() {
        User defaultUser = new User(0l, "defaultUser@tradingwaza.com", UserRoleEnum.ADMIN);
        return new SignedInUser(defaultUser);
    }

}
