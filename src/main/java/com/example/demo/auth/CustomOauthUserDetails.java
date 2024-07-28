package com.example.demo.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.repository.MembersEntity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class CustomOauthUserDetails implements UserDetails, OAuth2User{

	private final MembersEntity membersEntity;
	private Map<String, Object> attributes;

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return "ROLE_"+membersEntity.getRole();
			}
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return membersEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return membersEntity.getEmail();
	}

	//isAccountNonExpired() : 계정 만료 여부 => true : 만료 X
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//isAccountNonLocked() : 계정 잠김 여부 => true : 잠김 X
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//isCredentialsNonExpired() : 비밀번호 만료 여부 => true : 만료 X
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//isEnabled() : 계정 사용 가능 여부 => true : 사용 가능 O
	@Override
	public boolean isEnabled() {
		return true;
	}

}
