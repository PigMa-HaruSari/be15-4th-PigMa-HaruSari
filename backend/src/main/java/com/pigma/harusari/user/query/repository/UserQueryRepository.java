package com.pigma.harusari.user.query.repository;

import com.pigma.harusari.user.command.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQueryRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
