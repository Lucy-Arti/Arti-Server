package com.lucy.arti.designer.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.config.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Designer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designer_id")
    private Long id;

    private String userName;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String introduce;

    @OneToMany(mappedBy = "designer")
    private List<Clothes> clothes = new ArrayList<>();
}
