package com.lucy.arti.oauth.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.exception.BizException;
import com.lucy.arti.exception.ErrorMessage;
import com.lucy.arti.exception.MemberException;
import com.lucy.arti.jwt.CustomKakaoIdAuthToken;
import com.lucy.arti.jwt.RefreshToken;
import com.lucy.arti.jwt.RefreshTokenRepository;
import com.lucy.arti.jwt.TokenProvider;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.dto.MemberResponseDto;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.member.service.MemberService;
import com.lucy.arti.oauth.dto.KakaoLoginRequestDto;
import com.lucy.arti.oauth.dto.KakaoUserInfo;
import com.lucy.arti.oauth.dto.TokenDto;
import com.lucy.arti.oauth.repository.AuthorityRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
// 서비스단에서 쓰는 get 요청은 transsaction 필요 없음. post, put은 transaction 허용하겠다.
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;

    private final KakaoOauth2 kakaoOauth2;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String BEARER_PREFIX = "Bearer ";

    @Transactional
    public KakaoUserInfo kakaoLogin(KakaoLoginRequestDto kakaoLoginRequestDto) {
        String authorizedCode = kakaoLoginRequestDto.getAuthCode();
        KakaoUserInfo kakaoUserInfo = kakaoOauth2.getUserInfo(authorizedCode);
        log.info(kakaoUserInfo.getUsername());

        Long kakaoId = kakaoUserInfo.getId();
        String username = kakaoUserInfo.getUsername();
        String email = kakaoUserInfo.getEmail();
        String profile = kakaoUserInfo.getProfile();
        String nickname= memberService.generateRandomNickName();

        Optional<Member> byKakaoIdMember = memberRepository.findByKakaoId(kakaoId);
        if (byKakaoIdMember.isEmpty()) {
            log.info(username);
            Member newMember = new Member(kakaoId, username, email, profile, nickname);
            memberRepository.save(newMember);
        }

        return kakaoUserInfo;
    }

    public MemberResponseDto getByAccessToken(String accessToken) {
        Member member = memberRepository.findByAccessToken(accessToken);
        if (member != null) {
            return new MemberResponseDto(member.getId(), member.getUserName(), member.getEmail(),
                member.getProfile(), member.getLikes(),
                member.getVotes(), member.getWinners(), member.getAccessToken(),
                member.getAuthority(), member.getNickname());
        } else {
            throw new EntityNotFoundException(ErrorMessage.NOT_EXIST_USER.getReason());
        }
    }

    @Transactional
    public TokenDto createToken(KakaoUserInfo kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();

        CustomKakaoIdAuthToken customKakaoIdAuthToken = new CustomKakaoIdAuthToken(
            String.valueOf(kakaoId), "");
        Authentication authentication = authenticationManager.authenticate(customKakaoIdAuthToken);
        Member memberbyKakaoId = memberRepository.findByKakaoId(kakaoId)
            .orElse(null); // member에 member가 있거나, null이 있거나

        String accessToken = tokenProvider.createAccessTokenByKakaoId(kakaoId,
            memberbyKakaoId.getAuthorities());
        if (refreshTokenRepository.existsByKey(String.valueOf(kakaoId))) {
            refreshTokenRepository.deleteRefreshToken(
                refreshTokenRepository.findByKey(String.valueOf(kakaoId))
                    .orElseThrow(() -> new BizException(MemberException.NOT_FOUND_USER)));
        }
        String newRefreshToken = tokenProvider.createRefreshToken(kakaoId,
            memberbyKakaoId.getAuthorities());

        refreshTokenRepository.saveRefreshToken(
            RefreshToken.builder()
                .key(String.valueOf(kakaoId))
                .value(newRefreshToken)
                .build()
        );

        memberbyKakaoId.setAccessToken(accessToken);
        return tokenProvider.createTokenDTO(accessToken, newRefreshToken);
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public ResponseEntity<String> logout(Authentication authentication) {
        Long userKakaoId = Long.parseLong(authentication.getName());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(String.valueOf(userKakaoId))
            .orElseThrow(() -> new BizException(MemberException.LOGOUT_MEMBER));

        refreshTokenRepository.deleteRefreshToken(refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



