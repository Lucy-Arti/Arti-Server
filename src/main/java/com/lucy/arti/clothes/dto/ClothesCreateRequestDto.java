package com.lucy.arti.clothes.dto;

public record ClothesCreateRequestDto(
    String name,
    String type,
    String link,
    Long designerId
) {

}
