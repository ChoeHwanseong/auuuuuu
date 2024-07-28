package com.example.demo.auth;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class KakaoUserDetails implements OAuthUserInfo{
	
	private Map<String, Object> attributes;
	
	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		return (String) ((Map)attributes.get("kakao_account")).get("email");
	}

	@Override
	public String getName() {
		return (String) ((Map)attributes.get("properties")).get("nickname");
	}
	
}
