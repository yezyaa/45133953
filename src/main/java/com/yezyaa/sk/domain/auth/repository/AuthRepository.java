package com.yezyaa.sk.domain.auth.repository;

import com.yezyaa.sk.domain.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

}
