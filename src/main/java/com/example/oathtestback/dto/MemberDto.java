package com.example.oathtestback.dto;

import com.example.oathtestback.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MemberDto {
    private String email;

    private String password;

    private String name;

    public MemberDto of(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.email = member.getEmail();
        memberDto.password = member.getPassword();
        memberDto.name = member.getName();
        return memberDto;
    }
}
