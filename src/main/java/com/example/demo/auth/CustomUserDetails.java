package com.example.demo.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.repository.MembersEntity;

public class CustomUserDetails implements UserDetails{
	
	private MembersEntity membersEntity;
	public CustomUserDetails(MembersEntity membersEntity) {
		this.membersEntity = membersEntity;
	}

	//역할 반환
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
	
	//비밀번호 반환
	@Override
	public String getPassword() {
		return membersEntity.getPassword();
	}
	
	//이메일 반환
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
