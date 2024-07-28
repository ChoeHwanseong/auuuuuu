package com.example.demo.auth;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTTOKEN {
	private static SecretKey secretKey;
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
		
	public JWTTOKEN(@Value("${jwt.secret}")String secret) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String createJWT(String loginId, String role, Long expiredTime) {
		long currentTimeMillis = System.currentTimeMillis(); //토큰 생성시간
		expiredTime = currentTimeMillis + expiredTime; //토큰 만료 시간
		
		//Header에 안 실었는데.. 로그인 필터에서 실으니까 ㄱㅊ을거야~
		
		//JWT생성
		JwtBuilder builder = Jwts.builder()
				.claim("loginId", loginId)
				.claim("role", role)
				.issuedAt(new Date())
				.expiration(new Date(expiredTime))
				.signWith(secretKey, Jwts.SIG.HS256);
		
		return builder.compact();

	}/////createToken

	public Map<String,Object> getToKenPayloads(String token) {
		Map<String,Object> claims = new HashMap();
		
		try {
			claims = Jwts.parser()
					.verifyWith(secretKey).build() // 서명한 비밀키로 검증
					.parseSignedClaims(token)
					.getPayload();
			
		}catch (Exception e) {
			claims.put("invalid", "유효하지 않은 토큰");
		}
		
		return claims;
		//loginId를 얻고 싶으면 .get("loginId").toString()
		//role을 얻고 싶으면 .get("role").toString()
	}

	/**
	 * 유효한 토큰인지 검증하는 메소드
	 * @param token 발급토큰
	 * @return 유요한 토큰이면 true, 만료가 됬거나 변조된 토큰인 경우 false반환
	 */
	public static boolean verifyToken(String token) {
		try {
			//JWT토큰 파싱 및 검증
			Jws<Claims> claims= Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			System.out.println("만기일자:"+claims.getPayload().getExpiration());
			return true;
		}
		catch(JwtException | IllegalArgumentException e) {
			//System.out.println("유효하지 않은 토큰입니다:"+e);
		}
		return false;
	}
	
	/**
	 * 안쓰면 지울거야~
	 * @param request HttpServletRequest객체
	 * @param tokenName web.xml에 등록한 컨텍스트 초기화 파라미터 값(파라미터명은 "TOKEN-NAME")
	 * @return 발급받은 토큰 값 
	 */
	public static String getTokenInCookie(HttpServletRequest request, String tokenName) {
		Cookie[] cookies = request.getCookies();
		String token = "";
		if(cookies != null){
			for(Cookie cookie:cookies){
				if(cookie.getName().equals(tokenName)){
					token = cookie.getValue();
				}
			}
		}
		return token;
	}//
	
	
	
}
