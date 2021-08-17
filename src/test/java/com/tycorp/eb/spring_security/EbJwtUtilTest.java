package com.tycorp.eb.spring_security;

import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import com.tycorp.eb.domain.user.model.EbUser;
import com.tycorp.eb.spring_security.jwt_auth.EbJwtUtil;
import com.tycorp.eb.domain.user.model.LoginedEbUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Profile("test")
@DisplayName("com.tycorp.eb.spring_security.ebJwtUtilTest")
@SpringBootTest
public class EbJwtUtilTest {

    @Autowired
    private EbJwtUtil ebJwtUtil_ut;

    private LoginedEbUser loginedEbUser;

    private String subject = "cchan@eveningbrew.com";
    private String issuer = "com.tycorp.eb";

    @BeforeEach
    public void setup() {
        SubscriptionSlave slave = new SubscriptionSlave(new SubscriptionMaster());

        EbUser ebUser = new EbUser(
                Stream.of(slave).collect(Collectors.toSet()),
                "cchan@eveningbrew.com", "",
                "cchan");
        ebUser.setUserId(1l);

        loginedEbUser = new LoginedEbUser(ebUser);
    }

    @Test
    public void verifyThat_JwtUtil_ShouldReturnExpectedSubject_AfterGenerateJwtTokenAndParseJwtToken() {
        String jwtToken = ebJwtUtil_ut.generateJwtToken(loginedEbUser);
        Jws<Claims> claims = ebJwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getSubject().equals(subject));
    }

    @Test
    public void verifyThat_JwtUtil_ShouldReturnExpectedIssuer_AfterGenerateJwtTokenAndParseJwtToken() {
        String jwtToken = ebJwtUtil_ut.generateJwtToken(loginedEbUser);
        Jws<Claims> claims = ebJwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getIssuer().equals(issuer));
    }

}
