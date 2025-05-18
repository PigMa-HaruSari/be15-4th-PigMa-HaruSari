package com.pigma.harusari.user.command.repository;

import com.pigma.harusari.user.command.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCommandRepository extends JpaRepository<Member, Long> {
    /* 이메일 중복 검증 */
    boolean existsByEmail(String email);

    /* 회원에 대한 이메일 추출 */
    Optional<Member> findByEmail(String email);
}
