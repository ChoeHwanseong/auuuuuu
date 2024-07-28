package com.example.demo.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JWTFilter;
import com.example.demo.filter.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

	private final AuthenticationConfiguration configuration;
	private final JWTTOKEN jwttoken;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		
		http.authorizeHttpRequests(req-> req
			.requestMatchers("/admin").hasRole("ADMIN") // /admin으로 시작하는 페이지는 "ADMIN" ROLE을 가진 사람만
			.requestMatchers("/guide").hasAnyRole("ADMIN","GUIDE") // /guide으로 시작하는 페이지는 "ADMIN","GUIDE" ROLE을 가진 사람 들만
			//.anyRequest().authenticated()//위의 경로를 제외한 모든 요청에 대해서는 인증된 사용자만 접근
			.anyRequest().permitAll() //위의 경로를 제외한 모든 요청에 대해서는 접근허용
		);
		
		//로그인 설정
		http.formLogin(login->login
			.loginPage("/login") //로그인 페이지 설정
			.loginProcessingUrl("/loginProcess")//로그인 처리 URL(기본값:/login). 시큐리티가 로그인처리
			.permitAll()
				
		);
		
		http.oauth2Login(auth-> auth
				.loginPage("/oauth-login/login")
				.defaultSuccessUrl("/oath-login")
				.permitAll()
		);
		
		//로그아웃 설정
		http.logout(logout->logout
			.logoutUrl("/logout")//기본값 /logout						
		);
		
		/*csrf 비활성화
		rest api를 이용한 서버라면, session 기반 인증과는 다르게 stateless하기 때문에 서버에 인증정보를 보관하지 않는다.
		rest api에서 client는 권한이 필요한 요청을 하기 위해서는 요청에 필요한 인증 정보를(OAuth2, jwt토큰 등)을 포함시켜야 한다.
		따라서 서버에 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf 코드들을 작성할 필요가 없다.*/
		http.csrf(csrf->csrf.disable()); 
	
		http.sessionManagement(session->session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //JWT를 통한 인증, 인가 작업을 위해서는 세션을 무상태 (STATELESS) 로 설정하는 것이 중요!
			.maximumSessions(1)
		);
		
		// http basic 인증 방식 disable 설정 JWT, OAuth2 등 복잡한 인증 로직을 구현하려면 HTTP Basic 인증을 비활성화하는 것이 좋습니다.
		http.httpBasic(basic-> basic.disable());
		
		http.addFilterAt(new LoginFilter(authenticationManager(configuration), jwttoken), UsernamePasswordAuthenticationFilter.class);
		
		http.addFilterBefore(new JWTFilter(jwttoken), LoginFilter.class);
		
		return http.build();
	};
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	
}

