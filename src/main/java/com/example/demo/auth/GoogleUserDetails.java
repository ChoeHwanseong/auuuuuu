
package com.example.demo.auth;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class GoogleUserDetails implements OAuthUserInfo{
	
	private Map<String, Object> attributes;
	
	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("sub");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}
	
}
