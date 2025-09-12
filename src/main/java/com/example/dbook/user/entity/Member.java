package com.example.dbook.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
/*
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
*/
public class Member {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(Role.USER.toString()));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }
*/
}

