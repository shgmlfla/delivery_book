package com.example.dbook.auth.service;

import com.example.dbook.auth.dto.SignupRequestDto;
import com.example.dbook.common.exception.DuplicateEmailException;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto dto) {

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = Member.createMember(
                dto.getEmail(),
                encodedPassword,
                dto.getUsername(),
                dto.getNickname(),
                dto.getAddress(),
                dto.getGender(),
                dto.getAge()
        );

        memberRepository.save(member);
    }

}
