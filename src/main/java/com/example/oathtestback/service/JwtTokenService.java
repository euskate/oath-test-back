package com.example.oathtestback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    // JWT 인코더 : nimbus
    private final JwtEncoder jwtEncoder;

    // 토큰 생성 메서드
    public String createToken(Authentication authentication) {

        // 인가 범위 문자열 생성하여 반환 : ex) "ROLE_ADMIN ROLE_USER"
        String scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // 토큰 조각(claim 권리) 집합을 생성
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")                      // 발행자
                .issuedAt(Instant.now())             // 발행시간
                .expiresAt(Instant.now().plusSeconds(60 * 30))    // 만료시간 30분
                .subject(authentication.getName())    // sub: 유저이름
                .claim("scope", scope)    // 권한 범위  key : value
                .build();

        // claimSet을 기반으로 토큰을 암호화하고 문자열로 변환하여 반환
        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

}
