package com.example.oathtestback.entity;

import com.example.oathtestback.constant.Role;
import com.example.oathtestback.dto.MemberDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        this.id = null;
        this.email = memberDto.getEmail();
        this.password = passwordEncoder.encode(memberDto.getPassword());
        this.name = memberDto.getName();
        this.role = Role.ROLE_USER;
    }

    // 수정 업데이트
    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
