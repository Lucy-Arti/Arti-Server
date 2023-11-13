package com.lucy.arti.clothes.repository;


import com.lucy.arti.clothes.domain.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepositoryCustom extends JpaRepository<Clothes, Long> {
}
