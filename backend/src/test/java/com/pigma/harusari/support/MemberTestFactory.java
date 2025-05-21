package com.pigma.harusari.support;

import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

/* 테스트용 Member 객체 생성 메서드를 위한 팩토리 구현 */
public class MemberTestFactory {

    public static Member testInstanceWithId(Long memberId) {
        Member member = Member.builder()
                .email("test@domain.com")
                .password("encodedPassword")
                .nickname("테스터")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();

        ReflectionTestUtils.setField(member, "memberId", memberId);
        return member;
    }
}