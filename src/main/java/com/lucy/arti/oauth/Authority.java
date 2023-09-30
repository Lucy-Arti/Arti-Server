package com.lucy.arti.oauth;

import com.lucy.arti.member.model.UserRole;
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
    private Long id;

    @Column(name = "authority_name", length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole authorityName;


    public String getAuthorityName(){
        return this.authorityName.toString();
    }
}

