package com.tycorp.tb.spring_security;

import com.tycorp.tb.domain.SubscriptionMaster;
import com.tycorp.tb.domain.SubscriptionSlave;
import com.tycorp.tb.domain.User;
import com.tycorp.tb.spring_security.jwt_auth.JwtUtil;
import com.tycorp.tb.domain.SignedInUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("com.tycorp.tb.spring_security.JwtUtilTest")
@SpringBootTest
public class JwtUtilUnitTest {

    @Autowired
    private JwtUtil jwtUtil_ut;

    private SignedInUser signedInUser;

    private String subject = "cchan@tradingboard.com";
    private String issuer = "com.tycorp.ab";

    @BeforeEach
    public void setup() {
        SubscriptionSlave slave = new SubscriptionSlave(new SubscriptionMaster("default_master"));

        User user = new User(
                Stream.of(slave).collect(Collectors.toSet()),
                "cchan@tradingboard.com", "",
                "cchan");
        user.setUserId(1l);

        signedInUser = new SignedInUser(user);
    }

    @Test
    public void verifyThat_parseJwtToken_shouldReturnExpectedSubject_after_generateJwtTokenAnd() {
        String jwtToken = jwtUtil_ut.generateJwtToken(signedInUser);
        Jws<Claims> claims = jwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getSubject().equals(subject));
    }

    @Test
    public void verifyThat_parseJwtToken_shouldReturnExpectedIssuer_after_generateJwtTokenAndParseJwtToken() {
        String jwtToken = jwtUtil_ut.generateJwtToken(signedInUser);
        Jws<Claims> claims = jwtUtil_ut.parseJwtToken(jwtToken);

        assertTrue(claims.getBody().getIssuer().equals(issuer));
    }

}
