package com.lucy.arti.jwt.service;

import java.util.List;
import java.util.stream.Collectors;

import com.lucy.arti.exception.BizException;
import com.lucy.arti.exception.MemberException;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.oauth.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomKakaoAuthService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {

        return memberRepository.findByKakaoId(Long.valueOf(kakaoId))
                .map(this::createUserDetails)
                .orElseThrow(() -> new BizException(MemberException.NOT_FOUND_USER));

    }

    @Transactional(readOnly = true)
    public Member getMember(Long kakaoId) throws BizException {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new BizException(MemberException.NOT_FOUND_USER));
    }
    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authList = member.getAuthorities()
                .stream()
                .map(Authority::getAuthorityName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(String.valueOf(member.getKakaoId()), "", authList);
    }
}
