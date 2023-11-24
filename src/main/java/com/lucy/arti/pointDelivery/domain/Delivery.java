package com.lucy.arti.pointDelivery.domain;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.pointShop.domain.ShopItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    @Nullable
    private String address;

    private String phoneNumber;

    private String status;

    boolean delivery;

    @OneToOne
    @JoinColumn(name = "shop_item_id")
    private ShopItem item;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;
    @Builder
    public Delivery(String name, String address, String phoneNumber, boolean delivery, Member member, ShopItem item) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.delivery = delivery;
        this.member = member;
        this.item = item;
        this.created_at=LocalDateTime.now();
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

}
