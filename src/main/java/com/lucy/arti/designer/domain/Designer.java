package com.lucy.arti.designer.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.global.config.BaseTimeEntity;
import com.lucy.arti.global.config.Gender;
import lombok.Builder;
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

    @Column(nullable = false, length = 1000)
    private String introduce;

    @Column(name = "designer_profile")
    private String designerProfile;

    @OneToMany(mappedBy = "designer")
    private List<Clothes> clothes = new ArrayList<>();

    @Column(name = "instagram")
    private String instagram;

    @Builder
    public Designer(String userName, String introduce, String instagram, String designerProfile) {
        this.userName = userName;
        this.introduce = introduce;
        this.instagram = instagram;
        this.designerProfile = designerProfile;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateDesignerProfile(String designerProfile) {
        this.designerProfile = designerProfile;
    }

    public void updateInstagram(String instagram) {
        this.instagram = instagram;
    }

}
