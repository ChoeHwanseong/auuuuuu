package com.example.demo.controller;

import com.example.demo.repository.MembersEntity;
import com.example.demo.repository.MembersRepository;
import com.example.demo.auth.JWTTOKEN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MembersRepository membersRepository;
    private final JWTTOKEN jwtToken;

    /**
     * OAuth2 인증 후 사용자 정보를 처리하고 JWT 토큰을 발급합니다.
     * 
     * @param authentication OAuth2 인증 토큰
     * @return JWT 토큰 및 사용자 정보
     */
    @GetMapping("/oauth2/success")
    public ResponseEntity<Map<String, String>> handleOAuth2Login(@AuthenticationPrincipal OAuth2AuthenticationToken authentication) {
        // OAuth2 인증 후 사용자 정보 가져오기
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");

        if (email == null) {
            log.error("이메일 정보를 가져오지 못했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "이메일 정보를 가져오지 못했습니다."));
        }

        // 회원 정보를 데이터베이스에서 조회
        Optional<MembersEntity> optionalMember = membersRepository.findByEmail(email);

        MembersEntity member;

        if (optionalMember.isPresent()) {
            // 기존 회원인 경우
            member = optionalMember.get();
        } else {
            // 회원이 아닌 경우, 자동으로 회원가입 처리
            member = MembersEntity.builder()
                    .email(email)
                    .name(name)
                    .gender("male")
                    .birthday(java.sql.Date.valueOf(LocalDate.of(1996, 12, 7)))
                    .telnumber("010-2655-5346")
                    .address("관리자")
                    .password("") // 소셜 로그인일 경우 비밀번호는 빈 값
                    .role("USER") // 기본 역할 설정
                    .issocial(1) // 소셜 로그인 여부
                    .socialAt(LocalDateTime.now())
                    .build();
            membersRepository.save(member);
            log.info("새로운 회원이 등록되었습니다: " + email);
        }

        // JWT 토큰 생성
        String jwtTokenString = jwtToken.createJWT(member.getEmail(), member.getRole(), 3600000L); // 1시간 유효

        // JWT 토큰 및 사용자 정보를 응답으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTokenString);
        response.put("message", "Authentication successful");
        response.put("email", member.getEmail());
        response.put("name", member.getName());

        return ResponseEntity.ok(response);
    }
}
