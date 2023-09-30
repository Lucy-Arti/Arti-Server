package com.lucy.arti.oauth.service;

import com.lucy.arti.exception.BizException;
import com.lucy.arti.exception.MemberException;
import com.lucy.arti.jwt.CustomKakaoIdAuthToken;
import com.lucy.arti.jwt.RefreshToken;
import com.lucy.arti.jwt.RefreshTokenRepository;
import com.lucy.arti.jwt.TokenProvider;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.oauth.KakaoUserInfo;
import com.lucy.arti.oauth.dto.TokenDto;
import com.lucy.arti.oauth.dto.KakaoLoginRequestDto;
import com.lucy.arti.oauth.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // 서비스단에서 쓰는 get 요청은 transsaction 필요 없음. post, put은 transaction 허용하겠다.
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final KakaoOauth2 kakaoOauth2;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityReposistory;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String BEARER_PREFIX = "Bearer ";

    @Transactional // member Database에 저장해야 되기 때문
    public KakaoUserInfo kakaoLogin(KakaoLoginRequestDto kakaoLoginRequestDto) {
        String authorizedCode = kakaoLoginRequestDto.getAuthCode();

        KakaoUserInfo kakaoUserInfo = kakaoOauth2.getUserInfo(authorizedCode);

        Long kakaoId = kakaoUserInfo.getId();
        String username = kakaoUserInfo.getUsername();
        String email = kakaoUserInfo.getEmail();
        String profile = kakaoUserInfo.getProfile();

        Optional<Member> byKakaoIdMember = memberRepository.findByKakaoId(kakaoId);
        if (byKakaoIdMember.isEmpty()) {
            log.info(username);
            Member newMember = new Member(kakaoId, username, email, profile);

            memberRepository.save(newMember);
        }

//        Optional<Member> byKakaoIdMember = memberRepository.findByKakaoId(kakaoId);
//
//        if (byKakaoIdMember.isEmpty()) {
//            Optional<Authority> byAuthorityName = authorityReposistory.findByAuthorityName(UserRole.ROLE_KAKAO);
//            if (byAuthorityName.isEmpty()) {
//                throw new BizException(AuthorityException.NOT_FOUND_AUTHORITY);
//            }
//            Set<Authority> authorities = new HashSet<>();
//            authorities.add(byAuthorityName.get());
//            Member newMember = new Member(kakaoId, username, email, profile, authorities);
//            memberRepository.save(newMember);
//        }
        return kakaoUserInfo;
    }

    @Transactional
    public TokenDto createToken(KakaoUserInfo kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        CustomKakaoIdAuthToken customKakaoIdAuthToken = new CustomKakaoIdAuthToken(String.valueOf(kakaoId), "");
        Authentication authentication = authenticationManager.authenticate(customKakaoIdAuthToken);
        Member memberbyKakaoId = memberRepository.findByKakaoId(kakaoId).orElse(null); // member에 member가 있거나, null이 있거나

        String accessToken = tokenProvider.createAccessTokenByKakaoId(kakaoId, memberbyKakaoId.getAuthorities());
        if (refreshTokenRepository.existsByKey(String.valueOf(kakaoId))) {
            refreshTokenRepository.deleteRefreshToken(refreshTokenRepository.findByKey(String.valueOf(kakaoId))
                    .orElseThrow(() -> new BizException(MemberException.NOT_FOUND_USER)));
        }
        String newRefreshToken =  tokenProvider.createRefreshToken(kakaoId, memberbyKakaoId.getAuthorities());

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
        // bearer : 123123123123123 -> return 123123123123123123
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public ResponseEntity logout(String bearerToken){
        String accessToken = resolveToken(bearerToken);

        Long kakaoIdByToken = tokenProvider.getMemberKakaoIdByToken(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(String.valueOf(kakaoIdByToken))
                .orElseThrow(() -> new BizException(MemberException.LOGOUT_MEMBER));

        refreshTokenRepository.deleteRefreshToken(refreshToken);
        return new ResponseEntity(HttpStatus.OK);
    }
}



