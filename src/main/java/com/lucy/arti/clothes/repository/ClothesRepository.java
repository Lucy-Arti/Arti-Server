package com.lucy.arti.clothes.repository;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    @Query("SELECT c FROM Clothes c WHERE " +
            "c.name LIKE CONCAT('%',:query, '%')" +
            "Or c.designer.userName LIKE CONCAT('%', :query, '%')")
    List<Clothes> searchClothes(String query);

    List<Clothes> findByDesignerId(Long designerId);

    List<Clothes> findAllByType(Type type);

    default Clothes findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
            () -> BusinessException.from(ErrorCode.CLOTHES_NOT_FOUND));
    }
}
