package com.tycorp.eb.spring_security;

import com.tycorp.eb.domain.User;
import com.tycorp.eb.domain.UserRoleEnum;
import com.tycorp.eb.domain.SignedInUser;
import com.tycorp.eb.domain.SignedInUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

    public SignedInUserDetail getAuthenticatedUserDetail() {
        return (SignedInUserDetail)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public SignedInUserDetail getDefaultAuthenticatedUserDetail() {
        User defaultUser = new User(0l, "defaultUser@eveningbrew.com", UserRoleEnum.ADMIN);
        return new SignedInUser(defaultUser);
    }

}
