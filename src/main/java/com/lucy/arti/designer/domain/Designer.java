package com.lucy.arti.designer.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.config.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Designer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designer_id")
    private Long id;

    private String userName;

    @Column(nullable = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Column(nullable = false, length = 1000)
    private String introduce;

    @OneToMany(mappedBy = "designer")
    private List<Clothes> clothes = new ArrayList<>();
}
