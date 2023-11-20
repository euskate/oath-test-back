package com.example.oathtestback.controller;

import com.example.oathtestback.dto.MemberDto;
import com.example.oathtestback.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/api/member")
    public ResponseEntity register(MemberDto memberDto) {
        Long id = memberService.register(memberDto);
        return ResponseEntity.ok(id);
    }
}

