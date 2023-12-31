package com.lucy.arti.oauth.dto;

import com.lucy.arti.member.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authority")
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole authorityName;
}

