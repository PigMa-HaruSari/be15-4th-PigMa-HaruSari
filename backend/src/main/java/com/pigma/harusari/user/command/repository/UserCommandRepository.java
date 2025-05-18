package com.pigma.harusari.user.command.repository;

import com.pigma.harusari.user.command.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommandRepository extends JpaRepository<Member, Long> {

}
