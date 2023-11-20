package com.example.oathtestback.controller;

import com.example.oathtestback.dto.LoginRequestDto;
import com.example.oathtestback.dto.LoginResponseDto;
import com.example.oathtestback.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    // Jwt 토큰 생성 서비스
    private final JwtTokenService jwtTokenService;
    // 스프링 시큐리티 인증 관리자
    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String index() {
        return "Hello";
    }

    @GetMapping("/login")
    public String login() {
        return "login ok";
    }

    @PostMapping("/authenticate")
    public LoginResponseDto authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto);
        // 프론트엔드에서 받은 인증 정보로 인증 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // 인증 토큰에서 인증 정보를 반환한다.
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 인증 정보를 기반으로 토큰을 생성한다.
        String token = jwtTokenService.createToken(authenticate);

        // 인증정보를 받아서 토큰을 반환한다.
        return new LoginResponseDto(token);
    }

}
