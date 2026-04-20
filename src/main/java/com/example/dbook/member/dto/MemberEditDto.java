package com.example.dbook.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditDto {

    private String email;

    private String currentPassword;

    private String newPassword;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;
    private String address;

    private String gender;
    private String age;
}
