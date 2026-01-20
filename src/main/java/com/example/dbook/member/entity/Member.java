package com.example.dbook.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    // 회원 탈퇴 여부
    public enum MemberStatus {
        ACTIVE,
        WITHDRAWN   // 탈퇴
    } // 회원 탈퇴 여부

    public static Member signup(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .status(MemberStatus.ACTIVE)
                .build();
    }
}
