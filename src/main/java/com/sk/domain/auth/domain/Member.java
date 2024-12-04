package com.sk.domain.auth.domain;

import com.sk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "access_token")
    private String accessToken;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Member of(String email, String name, String password) {
        Member user = new Member();
        user.email = email;
        user.name = name;
        user.password = password;
        return user;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void clearAccessToken() {
        this.accessToken = null;
    }
}
