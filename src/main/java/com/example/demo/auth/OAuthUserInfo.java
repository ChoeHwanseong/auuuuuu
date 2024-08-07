package com.example.demo.auth;

public interface OAuthUserInfo {
	String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
