package com.lucy.arti.pointShop.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String brand;

    private String thumnail;

    @Nullable
    private String image;

    private String detail;

    private Long price;

    @Enumerated(EnumType.STRING)
    private ShopCategory category;

    private boolean delivery;
}
