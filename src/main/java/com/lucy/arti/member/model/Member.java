package com.lucy.arti.member.model;

import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.config.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
