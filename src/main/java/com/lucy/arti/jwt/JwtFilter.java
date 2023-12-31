package com.lucy.arti.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if(request.getServletPath().startsWith("/api/v1/kakao/login")) {
            filterChain.doFilter(request,response);
        } else if (request.getServletPath().startsWith("/api/v1/clothes")) {
            filterChain.doFilter(request, response);
        } else if(request.getServletPath().startsWith("/api/v2/shop")){
            filterChain.doFilter(request, response);
        } else {
            String token = resolveToken(request);

            log.debug("token  = {}",token);
            if(StringUtils.hasText(token)) {
                int flag = tokenProvider.validateToken(token);

                log.debug("flag = {}",flag);
                // 토큰 유효함
                if(flag == 1) {
                    this.setAuthentication(token);
                }else if(flag == 2) { // 토큰 만료
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter out = response.getWriter(); // 첫 호출
                    log.debug("doFilterInternal Exception CALL!");
                    log.info("{\"error\": \"ACCESS_TOKEN_EXPIRED\", \"message\" : \"엑세스토큰이 만료되었습니다.\"}");
                } else { //잘못된 토큰
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter out = response.getWriter();
                    log.debug("doFilterInternal Exception CALL!");
                    log.debug("{\"error\": \"BAD_TOKEN\", \"message\" : \"잘못된 토큰 값입니다.\"}");
//                    out.flush(); // 응답 데이터 전송
                }
            }
            else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
//                PrintWriter out = response.getWriter();
                log.debug("doFilterInternal Exception CALL!");
                log.debug("{\"error\": \"EMPTY_TOKEN\", \"message\" : \"토큰 값이 비어있습니다.\"}");
//                out.flush(); // 응답 데이터 전송
            }
            filterChain.doFilter(request, response);

        }
    }
    private void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthenticationByKakaoId(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        // bearer : 123123123123123 -> return 123123123123123123
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
