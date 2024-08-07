package com.example.demo.repository;


import java.time.LocalDateTime;
import java.util.Date;


import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersEntity {

	/** 회원 고유 번호. */
	@Id
	@SequenceGenerator(name="seq_members",sequenceName = "seq_members",allocationSize = 1,initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_members")
	private long id;

	/** 이메일. */
	@Column(nullable = false, length = 50)
	private String email;

	/** 이름. */
	@Column(nullable = false, length = 10)
	private String name;

	/** 비밀번호. */
	@Column(nullable = false, length = 20)
	private String password;

	/** 성별. */
	@Column(length = 5)
	private String gender;

	/** BIRTHDAY. */
	@Column(nullable = false)
	private Date birthday;

	/** 전화. */
	@Column(length = 18)
	private String telnumber;

	/** 주소. */
	@Column(length = 50)
	private String address;

	/** 생성일. */
	@Column
	@ColumnDefault("SYSDATE")
	@CreationTimestamp
	private LocalDateTime createdAt;

	/** 활성화 여부. */
	@Column
	@ColumnDefault("1")
	private Integer isactive;

	/** 수정 시간. */
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	/** 수정 여부. */
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer isupdate;

	/** 삭제날짜. */
	@Column(nullable = false)
	@ColumnDefault("0")
	private LocalDateTime deletedAt;

	/** 삭제 여부. */
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer isdelete;

	/** 소셜 연동 여부. */
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer issocial;

	/** 소셜 연동 날짜. */
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime socialAt;

	/** 가이드 인지. */
	@Column(length = 10)
	private String role;

	/** 가이드 자격증. */
	@Column(nullable = false)
	private String guidelicense;
	
	//CascadeType.All - Member의 변경이 소셜에도 전파
	//orphanRemoval - member엔터티 삭제시 소셜도 삭제
	@OneToOne(mappedBy = "members",cascade = CascadeType.ALL, orphanRemoval = true)
	private SocialUserEntity socialUser;

	

}