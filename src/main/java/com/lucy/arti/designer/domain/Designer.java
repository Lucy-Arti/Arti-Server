package com.lucy.arti.designer.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.global.config.BaseTimeEntity;
import com.lucy.arti.global.config.Gender;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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

    public List<Long> getClothesIdList() {
        List<Long> clothesIdList = new ArrayList<>();
        for (Clothes clothesItem : this.clothes) {
            clothesIdList.add(clothesItem.getId());
        }
        return clothesIdList;
    }

    private boolean isProduct(Clothes clothes) {
        return clothes.getType().equals(Type.product);
    }

    public Map<Long, String> getProductIdAndNames() {
        return this.clothes.stream()
            .filter(this::isProduct)
            .collect(Collectors.toMap(Clothes::getId, Clothes::getName)); // ID와 이름을 매핑하여 Map으로 수집
    }

    public Map<Long, String> getSketchIdAndNames() {
        return this.clothes.stream()
            .filter(clothesItem -> !isProduct(clothesItem))
            .collect(Collectors.toMap(Clothes::getId, Clothes::getName)); // ID와 이름을 매핑하여 Map으로 수집
    }

}
