package com.lucy.arti.jwt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name="refresh_token")
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="refresh_token_id")
    private Long id;

    @Column(name="rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    public void updateValue(String token){
        this.value = token;
    }

    @Builder
    public RefreshToken(String key, String value){
        this.key = key;
        this.value = value;
    }
}
