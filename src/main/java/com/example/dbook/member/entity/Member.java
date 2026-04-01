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

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String is_subscriber;

    private String address;

    @Column
    private String gender;

    @Column
    private Integer age;

    @Column
    private String billingKey;

    public void updateBillingKey(String billingKey){
        this.billingKey = billingKey;
        this.is_subscriber = "Y";
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    public enum MemberStatus {
        ACTIVE,
        WITHDRAWN   // 탈퇴
    }

    public static Member createMember(String email, String password, String username, String nickname, String address, String gender, Integer age) {
        return Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .address(address)
                .is_subscriber("N")
                .address(address)
                .gender(gender)
                .age(age)
                .status(MemberStatus.ACTIVE)
                .build();
    }
}
