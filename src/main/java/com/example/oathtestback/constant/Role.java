package com.example.oathtestback.constant;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "운영자"), USER("ROLE_USER", "사용자");

    private String key;
    private String name;

    Role(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
