package com.doyatama.university.payload.auth;

import com.doyatama.university.payload.UserSummary;

public class JwtAuthenticationResponse {
    private UserSummary userSummary;
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken, UserSummary userSummary) {
        this.accessToken = accessToken;
        this.userSummary = userSummary;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserSummary getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(UserSummary userSummary) {
        this.userSummary = userSummary;
    }
}
