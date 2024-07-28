package com.example.demo.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.repository.MembersEntity;
import com.example.demo.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private MembersRepository membersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//리포지토리 호출
		MembersEntity membersEntity= membersRepository.findByEmail(username).get();
		
		if(membersEntity != null) {
			return new CustomUserDetails(membersEntity);
		}
		
		return null;
	}

}
