package com.example.oathtestback.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;

import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
public class JwtConfig {

    // 비대칭 키 (공개키-개인키) 페어(한 쌍) 생성
    @Bean
    public KeyPair keyPair() {
        try {
            // RSA 알고리즘 - 키 길이를 2048자로 한 키 페어 생성
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // RSA Key 쌍 만들기 (Nimbus 라이브러리의 RSA 키)
    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {

        return new RSAKey
                .Builder((RSAPublicKey)keyPair.getPublic())     // 공개키
                .privateKey(keyPair.getPrivate())               // 개인키
                .keyID(UUID.randomUUID().toString())            // Random Key ID
                .build();
    }

    // RSA Key를 활용하여 Nimbus에서 제공하는 JWK(JSON Web Key)Set을 생성
    // https://datatracker.ietf.org/doc/html/rfc7517
    @Bean
    public JWKSource jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);

        return new JWKSource() {
            @Override
            public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
                return jwkSelector.select(jwkSet);
            }
        };

    }

    // JWT 키 암호화(생성) : nimbus를 사용
    @Bean
    public JwtEncoder jwtEncoder(JWKSource jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // JWT 키 복호화(검증) : nimbus를 사용
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();

    }
}