package org.example.nextchallenge.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.auth.dto.UserLoginRequestDto;
import org.example.nextchallenge.auth.dto.UserLoginResponseDto;
import org.example.nextchallenge.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authService.login(requestDto);
    }
}
