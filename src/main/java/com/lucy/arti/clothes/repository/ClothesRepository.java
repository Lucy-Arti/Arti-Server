package com.lucy.arti.clothes.repository;

import com.lucy.arti.clothes.domain.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<Clothes, Long>, ClothesRepositoryCustom {

}
