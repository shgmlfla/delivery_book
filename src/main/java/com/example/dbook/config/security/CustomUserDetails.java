package com.example.dbook.config.security;

import com.example.dbook.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Member member){
        this.member = member;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
    }

    public CustomUserDetails(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
        this.member = Member.builder()
                .id(id)
                .email(email)
                .build();
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Member getUser() {
        return member;
    }

    public Long getId(){
        return member.getId();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
