package com.example.oathtestback.service;

import com.example.oathtestback.config.auth.OAuthAttributes;
import com.example.oathtestback.dto.MemberDto;
import com.example.oathtestback.entity.Member;
import com.example.oathtestback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(MemberDto memberDto) {
        Member saved = memberRepository.save(new Member(memberDto, passwordEncoder));
        return saved.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).get();

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2 정보를 가져옵니다.

        Map<String, Object> originAttributes = oAuth2User.getAttributes();  // OAuth2User의 attribute

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();    // 소셜 정보를 가져옵니다.

        // OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, originAttributes);

        Member findMember = memberRepository.findByEmail(attributes.getEmail())
                .map(member -> member.update(attributes.getName(), attributes.getProfileImageUrl()))
                .orElse(attributes.toEntity());
        Member member = memberRepository.save(findMember);
        String email = member.getEmail();

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())), attributes.getAttributes(), attributes.getNameAttributesKey());

    }
}
