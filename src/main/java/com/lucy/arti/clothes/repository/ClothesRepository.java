package com.lucy.arti.clothes.repository;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    @Query("SELECT c FROM Clothes c WHERE " +
            "c.name LIKE CONCAT('%',:query, '%')" +
            "Or c.designer.userName LIKE CONCAT('%', :query, '%')")
    List<Clothes> searchClothes(String query);

    List<Clothes> findByDesignerId(Long designerId);
}
