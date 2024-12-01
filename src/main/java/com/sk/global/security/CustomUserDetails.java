package com.sk.global.security;

import com.sk.domain.auth.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

// User 클래스를 상속하여 추가 필드(memberId)를 포함한 사용자 정보를 제공하도록 확장
@Getter
public class CustomUserDetails extends User {
    private final Long memberId;

    public CustomUserDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities != null ? authorities : Collections.emptyList());
        this.memberId = member.getId();
    }
}