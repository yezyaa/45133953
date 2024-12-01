package com.sk.global.security.annotations;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

// 로그인된 사용자의 정보를 주입받기 위한 커스텀 어노테이션
@Target(ElementType.PARAMETER) // 메서드 파라미터에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임에 사용
@Documented
@AuthenticationPrincipal // Spring Security의 인증 객체를 주입
public @interface CurrentUser {
}
