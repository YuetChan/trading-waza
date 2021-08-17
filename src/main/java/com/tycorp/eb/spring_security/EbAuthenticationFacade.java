package com.tycorp.eb.spring_security;

import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.EbUserRoleEnum;
import com.tycorp.eb.domain.user.model.LoginedEbUser;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EbAuthenticationFacade {

    public LoginedEbUserDetail getAuthenticatedEbUserDetail() {
        return (LoginedEbUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public LoginedEbUserDetail getDefaultAuthenticatedEbUserDetail() {
        EbUser defaultUser = new EbUser(0l, "defaultUser@eveningbrew.com", EbUserRoleEnum.ADMIN);
        return new LoginedEbUser(defaultUser);
    }

}
