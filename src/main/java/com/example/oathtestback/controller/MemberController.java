package com.example.oathtestback.controller;

import com.example.oathtestback.dto.MemberDto;
import com.example.oathtestback.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @GetMapping("/oauth/loginInfo")
    public String oauthLoginInfo(Authentication authentication){
        //oAuth2User.toString() 예시 : Name: [2346930276], Granted Authorities: [[USER]], User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //attributes.toString() 예시 : {id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return attributes.toString();
    }

}
