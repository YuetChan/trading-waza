package com.tycorp.eb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
public class JwtConfig {

    @Value("${keystoreAlias}")
    private String keystoreAlias;
    @Value("${keystorePwd}")
    private String keystorePwd;

    @Bean(name = "loadJwtPrivateKey")
    public Key loadJwtPrivateKey()
            throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException,
            IOException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(getClass().getClassLoader().getResourceAsStream("evening-brew.jks"), keystorePwd.toCharArray());

        return keyStore.getKey(keystoreAlias, keystorePwd.toCharArray());
    }

    @Bean(name = "loadJwtPublicKey")
    public PublicKey loadJwtPublicKey() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(getClass().getClassLoader().getResourceAsStream("evening-brew.cer"));

        return cert.getPublicKey();
    }

}
