package com.tycorp.eb.spring_security;

import com.tycorp.eb.domain.SubscriptionMaster;
import com.tycorp.eb.domain.SubscriptionSlave;
import com.tycorp.eb.domain.User;
import com.tycorp.eb.spring_security.jwt_auth.JwtUtil;
import com.tycorp.eb.domain.SignedInUser;
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
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil_ut;

    private SignedInUser signedInUser;

    private String subject = "cchan@eveningbrew.com";
    private String issuer = "com.tycorp.eb";

    @BeforeEach
    public void setup() {
        SubscriptionSlave slave = new SubscriptionSlave(new SubscriptionMaster());

        User user = new User(
                Stream.of(slave).collect(Collectors.toSet()),
                "cchan@eveningbrew.com", "",
                "cchan");
        user.setUserId(1l);

        signedInUser = new SignedInUser(user);
    }

    @Test
    public void verifyThat_JwtUtil_ShouldReturnExpectedSubject_AfterGenerateJwtTokenAndParseJwtToken() {
        String jwtToken = jwtUtil_ut.generateJwtToken(signedInUser);
        Jws<Claims> claims = jwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getSubject().equals(subject));
    }

    @Test
    public void verifyThat_JwtUtil_ShouldReturnExpectedIssuer_AfterGenerateJwtTokenAndParseJwtToken() {
        String jwtToken = jwtUtil_ut.generateJwtToken(signedInUser);
        Jws<Claims> claims = jwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getIssuer().equals(issuer));
    }

}
