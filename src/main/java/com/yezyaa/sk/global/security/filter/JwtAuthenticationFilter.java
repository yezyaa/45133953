package com.yezyaa.sk.global.security.filter;

import com.yezyaa.sk.domain.auth.domain.Member;
import com.yezyaa.sk.domain.auth.exception.UserNotFoundException;
import com.yezyaa.sk.domain.auth.repository.AuthRepository;
import com.yezyaa.sk.global.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request); // 요청 헤더에서 AccessToken 추출

        if (token != null && jwtTokenProvider.validateToken(token)) { // AccessToken 검증

            Long userId = jwtTokenProvider.extractUserId(token); // 토큰에서 사용자 ID 추출

            Member member = authRepository.findById(userId) // 사용자 조회
                    .orElseThrow(UserNotFoundException::new);

            // 로그아웃 여부 확인
            if (member.getAccessToken() == null || !token.equals(member.getAccessToken())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("로그아웃된 사용자입니다.");
                return;
            }

            // SecurityContext에 인증 정보 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
