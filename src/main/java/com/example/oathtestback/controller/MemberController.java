package com.example.oathtestback.controller;

import com.example.oathtestback.dto.MemberDto;
import com.example.oathtestback.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/member")
    public ResponseEntity register(MemberDto memberDto) {
        Long id = memberService.register(memberDto, passwordEncoder);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/member/myPage")
    public ResponseEntity myPage(Principal principal) {
        String email = principal.getName();
        MemberDto memberDto = memberService.myPage(email);
        return ResponseEntity.ok(memberDto);
    }
}

