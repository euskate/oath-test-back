package com.example.oathtestback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 로그인 인증 성공시 토큰을 반환하는 DTO
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginRequestDto {
    String username;
    String password;
}
