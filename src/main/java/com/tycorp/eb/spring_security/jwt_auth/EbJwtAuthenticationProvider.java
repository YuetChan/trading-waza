package com.tycorp.eb.spring_security.jwt_auth;

import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.domain.user.model.LoginedEbUser;
import com.tycorp.eb.domain.user.repository.EbUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EbJwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private EbUserRepository userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Jws<Claims> claims = EbJwtUtil.parseJwtToken(((EbJwtAuthenticationToken) authentication).getJwtToken());
            EbUser user = userRepo.findByUseremail(claims.getBody().getSubject().toString()).orElseThrow(
                    () -> new UsernameNotFoundException("Invalid crediential"));

            return new UsernamePasswordAuthenticationToken(new LoginedEbUser(user), null, new ArrayList<>());
        }catch(Exception e) {
            throw new BadCredentialsException("Invalid/Missing JWT");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EbJwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
