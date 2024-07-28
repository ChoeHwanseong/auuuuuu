package com.example.demo.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.auth.JWTTOKEN;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	private final JWTTOKEN jwttoken;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String loginId = obtainUsername(request);
		String password = obtainPassword(request);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password, null);
		return authenticationManager.authenticate(authenticationToken);
	}

	//로그인 성공 시 
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//Email추출
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
		String email = customUserDetails.getUsername();
		
		//role 추출
		Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();
		
		//JWT에 토큰 생성 요청 1시간짜리
		String token = jwttoken.createJWT(email, role, 60*60*1000L);
		
		//JWT를 response에 담아서 응답(header 부분에)
		// key : "Authorization"
        // value : "Bearer " (인증방식) + token
		response.addHeader(jwttoken.AUTHORIZATION, jwttoken.BEARER + token);
		
	}

	//로그인 실패시
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		 response.setStatus(401);
	}

	
}
