package org.example.nextchallenge.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponseDto {
    private String accessToken;
    private String username;
    private String loginId;
    private String role;
}
