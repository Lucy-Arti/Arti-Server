package com.lucy.arti.jwt;

import com.lucy.arti.exception.BizException;
import com.lucy.arti.jwt.service.CustomKakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomKakaoIdAuthProvider implements AuthenticationProvider {

//    private final PasswordEncoder passwordEncoder;
    private final CustomKakaoAuthService customKakaoAuthService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("authenticate 오나요?"+ authentication.getName());
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        } catch (BizException exception) {
            throw exception;
        }

        Object principalToReturn = user;
        log.info("만들기전");
        CustomKakaoIdAuthToken result = new CustomKakaoIdAuthToken(principalToReturn, authentication.getCredentials()
                , this.authoritiesMapper.mapAuthorities(user.getAuthorities()));

        log.info("result");
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomKakaoIdAuthToken.class);
    }

    protected final UserDetails retrieveUser(String kakaoId) throws BizException {
        try {
            UserDetails loadedUser = customKakaoAuthService.loadUserByUsername(kakaoId);
            System.out.println("loadedUser = " + loadedUser);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (BizException ex) {
            log.debug("error in retrieveUser = {}", ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(
                    "내부 인증 로직중 알수 없는 오류가 발생하였습니다.");
        }
    }
}
