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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum MemberStatus {
        ACTIVE,
        WITHDRAWN   // 탈퇴
    }

    public enum Role{
        USER,
        ADMIN
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
                .role(Role.USER)
                .build();
    }

    public void changeRole(Role role){
        this.role = role;
    }

    public void updatePassword(String encodedPassword) {
        if (encodedPassword != null && !encodedPassword.isBlank()) {
            this.password = encodedPassword;
        }
    }

    public void updateNickName(String nickname){
        if (nickname != null && !nickname.isBlank()){
            this.nickname = nickname;
        }
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateSubscriptionStatus(String is_subscriber){
        if(!"Y".equals(is_subscriber) && !"N".equals(is_subscriber)){
            throw new IllegalArgumentException("올바르지 않은 구독 status 입니다. (Y/N)");
        }
        this.is_subscriber = is_subscriber;
    }
}
