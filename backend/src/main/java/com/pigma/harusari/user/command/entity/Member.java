package com.pigma.harusari.user.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.ORDINAL) // DB에는 0, 1, 2를 순서대로 tinyint로 저장
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "consent_personal_info", nullable = false)
    private Boolean consentPersonalInfo = false;

    @Column(name = "user_registered_at", nullable = false)
    private LocalDateTime userRegisteredAt;

    @Column(name = "user_modified_at")
    private LocalDateTime userModifiedAt;

    @Column(name = "user_deleted_at")
    private Boolean userDeletedAt = false;

    @Builder
    public Member(String email, String password, String nickname, Gender gender, Boolean consentPersonalInfo, LocalDateTime userRegisteredAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.consentPersonalInfo = (consentPersonalInfo != null) ? consentPersonalInfo : false;
        this.userRegisteredAt = (userRegisteredAt != null) ? userRegisteredAt : LocalDateTime.now();
        this.userDeletedAt = false;
    }

}