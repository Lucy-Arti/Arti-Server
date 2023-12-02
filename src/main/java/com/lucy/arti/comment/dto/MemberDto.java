package com.lucy.arti.comment.dto;

import com.lucy.arti.member.domain.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class MemberDto {
    private String userName;
    private String profile;
    private String customProfile;
    private String nickname;


    public static MemberDto fromMember(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName(member.getUserName());
        memberDto.setProfile(member.getProfile());
        memberDto.setNickname(member.getNickname());
        memberDto.setCustomProfile((member.getCustomProfile()));
        return memberDto;
    }
}
