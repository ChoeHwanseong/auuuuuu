package com.example.demo.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MembersEntity;
import com.example.demo.repository.MembersRepository;
import com.example.demo.repository.SocialUserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
Form로그인 방식은 기존의 CustomUserDetailsService를 통해서 진행됨.
첫 로그인이면 자동으로 회원가입 진행
*/
@Service
@RequiredArgsConstructor
@Slf4j //로깅용 어노테이션
public class CustomOauthUserService extends DefaultOAuth2UserService{
	
	private final MembersRepository membersRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("getAttributes : {}", oAuth2User.getAttributes()); //Info	명확한 의도가  있는 정보성 로그요구사항에 따라 시스템 동작을 보여줄 떄
		String provider = userRequest.getClientRegistration().getRegistrationId();
		OAuthUserInfo oAuthUserInfo = null;
		
		if(provider.equals("google")) { //구글이라면
			log.info("구글 로그인");
			oAuthUserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
			
		}
		else if(provider.equals("kakao")) { //카카오라면
			log.info("카카오 로그인");
			oAuthUserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
			
		}
		else if(provider.equals("naver")) { //네이버라면
			log.info("네이버 로그인");
			oAuthUserInfo = new NaverUserDetails(oAuth2User.getAttributes());
			
		}
		
		String providerId = oAuthUserInfo.getProviderId();
		String email = oAuthUserInfo.getEmail();
		String name = oAuthUserInfo.getName();
		
		MembersEntity findMember = membersRepository.findByEmail(email).get();
		MembersEntity membersEntity = null;
		
		if(findMember == null) {
			membersEntity = membersEntity.builder()
							.email(email)
							.name(name)
							.role("user")
							.socialUser(SocialUserEntity.builder()
									.provider(providerId)
									.providerId(providerId)
									.build())
							.build();	
			membersRepository.save(membersEntity);
		}
		else {
			membersEntity=findMember;
		}
		
		return new CustomOauthUserDetails(membersEntity, oAuth2User.getAttributes());
	}
	
	
}
