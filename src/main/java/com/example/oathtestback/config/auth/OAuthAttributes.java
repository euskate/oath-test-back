package com.example.oathtestback.config.auth;

import com.example.oathtestback.constant.Role;
import com.example.oathtestback.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OAuthAttributes {
    private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private String nameAttributesKey;
    private String name;
    private String email;
    private String profileImageUrl;

    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
      if ("google".equals(socialName)) {
        return ofGoogle("sub", attributes);
    } else if ("naver".equals(socialName)) {
        return ofNaver("id", attributes);
    }
      return null;
    }

    // 구글 서비스에서 정보 가져오기
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    // 네이버 서비스에서 정보 가져오기
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name(String.valueOf(response.get("nickname")))
                .email(String.valueOf(response.get("email")))
                .profileImageUrl(String.valueOf(response.get("profile_image")))
                .attributes(response)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    // 멤버 엔티티로 바꾸기
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .role(Role.ROLE_USER)
                .build();
    }


}



    
    