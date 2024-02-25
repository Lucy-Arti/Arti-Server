package com.lucy.arti.clothes.repository;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// 페이지네이션에 사용
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    @Query("SELECT c FROM Clothes c WHERE " +
        "c.name LIKE CONCAT('%',:query, '%')" +
        "Or c.designer.userName LIKE CONCAT('%', :query, '%')")
    List<Clothes> searchClothes(String query);

    List<Clothes> findByDesignerId(Long designerId);

    List<Clothes> findAllByType(Type type);
    Page<Clothes> findAllByType(Type type, Pageable pageable);

    default Clothes findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
            () -> BusinessException.from(ErrorCode.CLOTHES_NOT_FOUND));
    }
}
