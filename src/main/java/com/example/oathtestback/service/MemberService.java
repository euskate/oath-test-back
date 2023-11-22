package com.example.oathtestback.service;

import com.example.oathtestback.dto.MemberDto;
import com.example.oathtestback.entity.Member;
import com.example.oathtestback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // 회원가입
    public Long register(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Member member = new Member(memberDto, passwordEncoder);
        validateDuplicateMember(member);
        Member saved = memberRepository.save(member);
        return saved.getId();
    }

    // 중복회원 검증
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        // 가입을 위해 입력한 멤버가 존재한다면
        if (findMember != null) {
            // 예외 발생
            throw new IllegalStateException("가입된 회원입니다.");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // DB에서 회원 정보 조회 (e-mail)
        Member member = memberRepository.findByEmail(email);

        // 이메일에 해당하는 사용자가 없을 경우 예외발생
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        // 스프링 시큐리티 사용자 객체를 반환
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    public MemberDto myPage(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            new RuntimeException("회원을 찾을 수 없음");
        }
        MemberDto memberDto = MemberDto.of(member);
        return memberDto;
    }
}
