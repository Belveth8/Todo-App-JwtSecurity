package com.bitcamp.todo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 토큰 필터 역할
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // request 요청에서 토큰 가져오기
            String token = ParseBearerToken(request);
            log.info("Filter is running...");

            // 가져온 토큰 검증하기 및 시큐리티에 등록
            // JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
            if(token != null && !token.equalsIgnoreCase("null")) {
                // user id 빼오기 / 위조 된 경우 예외 처리 된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated userID : " + userId);

                // 인증완료 : SecurityContextHolder 에
                // 등록해야 인증된 user 라고 판단
                AbstractAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userId, // 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다.
                                null,
                                AuthorityUtils.NO_AUTHORITIES);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // 초기화
                securityContext.setAuthentication(authenticationToken);

                SecurityContextHolder.setContext(securityContext);
            }

        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    // http 요청의 Header를 파싱해서 Bearer 토큰을 리턴한다.
    // view 단에서도 똑같이 맵핑해줘야 한다.
    private String ParseBearerToken (HttpServletRequest request) {
        // http 요청 Header에서 파싱
        String bearerToken = request.getHeader("Authorization");  // token의 key name

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }




}
