package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MembersEntity;
import com.example.demo.repository.MembersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembersService {
	
	private final MembersRepository membersRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public boolean signup(MembersDTO dto) {
		
		boolean isDuplicated = membersRepository.existsByEmail(dto.getEmail());
		if(isDuplicated) return false;
		
		//암호화
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		MembersEntity entity = dto.toEntity();
		
		//역할 DTO에서 받아왔잖아~
		
		membersRepository.save(entity);
		return true;
		
	}

	public MembersDTO getLoginMemberByLoginId(String loginId) {
		if(loginId == null) return null;
		MembersEntity membersEntity= membersRepository.findByEmail(loginId).get();
		return MembersDTO.toDto(membersEntity);
	}
	
	

}
