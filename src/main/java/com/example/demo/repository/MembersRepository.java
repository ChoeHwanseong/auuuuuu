package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepository extends JpaRepository<MembersEntity,Long> {

	//아이디 조회용(단일 레코드 반환:Optional<엔터티타입>)
	Optional<MembersEntity> findByEmail(String username);

	//이메일 존재 여부용(WHERE eMail=?):서비스단의 중복아이디 검증용
	boolean existsByEmail(String email);

}
