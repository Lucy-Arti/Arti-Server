package com.lucy.arti.comment.dto;

import com.lucy.arti.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String userName;
    private String profile;
    private String customProfile;

    public static MemberDto fromMember(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName(member.getUserName());
        memberDto.setProfile(member.getProfile());
        memberDto.setCustomProfile((memberDto.getCustomProfile()));
        return memberDto;
    }
}
