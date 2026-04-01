package com.example.dbook.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class SignupRequestDto {

    @Schema(description = "이메일")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(description = "사용자 실명")
    private String username;
    
    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "성별")
    private String gender;

    @Schema(description = "나이", example = "20")
    private Integer age;

}
