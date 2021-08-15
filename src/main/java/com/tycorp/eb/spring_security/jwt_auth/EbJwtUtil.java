package com.tycorp.eb.spring_security.jwt_auth;

import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EbJwtUtil {

    @Resource(name="loadJwtPublicKey")
    private Key localJwtPublicKey;
    @Resource(name="loadJwtPrivateKey")
    private Key localJwtPrivateKey;

    private static Key jwtPublicKey;
    private static Key jwtPrivateKey;

    @PostConstruct
    public void init() {
        jwtPublicKey = localJwtPublicKey;
        jwtPrivateKey = localJwtPrivateKey;
    }

    public static Jws<Claims> parseJwtToken(String jwtToken) {
        return Jwts.parser().setSigningKey(EbJwtUtil.jwtPublicKey).parseClaimsJws(jwtToken);
    }

    public static String generateJwtToken(LoginedEbUserDetail loginedEbUserDetail) {
        Map<String, Object> extraClaims = new HashMap<>();

        return Jwts.builder()
                .setSubject(loginedEbUserDetail.getUseremail())
                .setIssuer("com.tycorp.eb").setIssuedAt(Date.from(Instant.now()))
                .setNotBefore(getDefaultNotBeforeDate())
                .setExpiration(getDefaultExpiratioDate())
                .addClaims(extraClaims)
                .signWith(SignatureAlgorithm.RS512, EbJwtUtil.jwtPrivateKey)
                .compact();
    }

    public static String generateJwtTokenForTesting() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        return Jwts.builder()
                .setSubject("cchan@eveningbrew.com")
                .setIssuer("com.tycorp.eb").setIssuedAt(Date.from(Instant.now()))
                .setNotBefore(getDefaultNotBeforeDate())
                .setExpiration(getDefaultExpiratioDate())
                .addClaims(extraClaims)
                .signWith(SignatureAlgorithm.RS512, EbJwtUtil.jwtPrivateKey)
                .compact();
    }

    private static Date getDefaultExpiratioDate() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }
    private static Date getDefaultNotBeforeDate() {
        return Date.from(Instant.now());
    }

}
