package com.tycorp.eb.spring_security.jwt_auth;

import com.tycorp.eb.domain.User;
import com.tycorp.eb.domain.SignedInUser;
import com.tycorp.eb.repository.UserRepository;
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
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Jws<Claims> claims = JwtUtil.parseJwtToken(((JwtAuthenticationToken) authentication).getJwtToken());
            User user = userRepo.findByUseremail(claims.getBody().getSubject().toString())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid crediential"));

            return new UsernamePasswordAuthenticationToken(new SignedInUser(user), null, new ArrayList<>());
        }catch(Exception e) {
            throw new BadCredentialsException("Invalid/Missing JWT");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
