package com.example.oathtestback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 로그인 내용을 전달하는 DTO
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginResponseDto {
    String token;
}
