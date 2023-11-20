package com.lucy.arti.pointShop.repository;

import com.lucy.arti.pointShop.domain.ShopCategory;
import com.lucy.arti.pointShop.domain.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopItem,Long> {

    List<ShopItem> findByCategory(ShopCategory category);

    Optional<ShopItem> findById(Long id);

}
