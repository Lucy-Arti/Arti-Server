package com.lucy.arti.member.dto;

public record MemberUpdateResponseDto(
    Long id,
    Long kakaoId,
    String nickName
) {

}
