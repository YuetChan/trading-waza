package com.tycorp.tb.spring_security;

import com.tycorp.tb.domain.User;
import com.tycorp.tb.domain.UserRoleEnum;
import com.tycorp.tb.domain.SignedInUser;
import com.tycorp.tb.domain.SignedInUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

    public SignedInUserDetail getAuthenticatedUserDetail() {
        return (SignedInUserDetail)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public SignedInUserDetail getDefaultAuthenticatedUserDetail() {
        User defaultUser = new User(0l, "defaultUser@tradingboard.com", UserRoleEnum.ADMIN);
        return new SignedInUser(defaultUser);
    }

    public SignedInUserDetail getDefaultAuthenticatedUserDetail_test() {
        User defaultUser = new User(0l, "defaultUser@tradingboard.com", UserRoleEnum.ADMIN);
        return new SignedInUser(defaultUser);
    }

}
