package com.personal.kakaoLogin.oAuthLogin.entity;

import lombok.Data;

@Data
public class User {
    private Long Id;
    private String email;
    private String nickName;
    private String imageURL;
    private String name;
    private String phoneNumber;
    private String encryptedJumin;
    private String accessToken;
    private String refreshToken;

    public User(String email, String nickName, String accessToken) {
        this.email = email;
        this.nickName = nickName;
        this.accessToken = accessToken;
    }
}
