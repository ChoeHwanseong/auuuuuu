package com.example.demo.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserEntity {
	
	@Id
    @Column(name = "members_id")
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "members_id")
    private MembersEntity members;
    
    private String provider;
    
    private String providerId;
	
}
