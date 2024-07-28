package com.example.demo.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.auth.JWTTOKEN;
import com.example.demo.repository.MembersEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter{
	private final JWTTOKEN jwttoken;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//request에서 Authorization헤더 찾기
		String authorization = request.getHeader(jwttoken.AUTHORIZATION);
		
		//Authorization헤더 검증
		if(authorization == null || !authorization.startsWith(jwttoken.BEARER)) {
			//헤더가 비었거나 bearer시작안하면 필터를 다음으로 넘긴다.
			filterChain.doFilter(request, response);
			
			return;
		}
		
		String token = authorization.split(" ")[1]; //bearer 제거
		
		//토큰 검증
		if(jwttoken.verifyToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//토큰 검증 후 일시적 session 생성하고 user정보 설정
		String loginId = jwttoken.getToKenPayloads(token).get("loginId").toString();
		String role = jwttoken.getToKenPayloads(token).get("role").toString();
		
		MembersEntity entity = MembersEntity.builder()
									.email(loginId)
									.password("임시 비밀번호")// 매번 요청마다 DB 조회해서 password 초기화 할 필요 x => 정확한 비밀번호 넣을 필요 없음 따라서 임시 비밀번호 설정!
									.role(role).build();
		
		// UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(entity);
		
		 // 스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		
		// 세션에 사용자 등록 => 일시적으로 user 세션 생성
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		filterChain.doFilter(request, response);
		
		
	}
	
	
}
