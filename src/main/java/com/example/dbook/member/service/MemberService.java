package com.example.dbook.member.service;

import com.example.dbook.auth.dto.SignupRequestDto;
import com.example.dbook.auth.dto.SignupResponseDto;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto request){
        if (memberRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .nickname(request.getNickname())
                .address(request.getAddress())
                .gender(request.getGender())
                .age(request.getAge())
                .is_subscriber("N")
                .status(Member.MemberStatus.ACTIVE)
                .build();

        memberRepository.save(member);

        return new SignupResponseDto(
                member.getId(),
                member.getEmail()
        );

    }
}
