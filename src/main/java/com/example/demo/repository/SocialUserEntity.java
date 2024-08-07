package com.example.demo.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 소셜 사용자 정보 테이블 모델 클래스.
 * 
 * 소셜 로그인 정보를 저장합니다.
 */
@Entity
@Table(name = "social_users") // 테이블 이름을 명시적으로 설정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserEntity {

    /** 소셜 사용자 ID (회원 ID와 동일) */
    @Id
    @Column(name = "members_id")
    private Long id;

    /** 회원 정보와의 일대일 관계 */
    @OneToOne
    @MapsId // MembersEntity의 기본 키를 공유
    @JoinColumn(name = "members_id", referencedColumnName = "id")
    private MembersEntity members;

    /** 소셜 로그인 제공자 */
    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    /** 소셜 제공자에서의 사용자 ID */
    @Column(name = "provider_id", nullable = false, length = 100, unique = true)
    private String providerId;
}
