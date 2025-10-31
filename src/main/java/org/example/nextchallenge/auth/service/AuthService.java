package org.example.nextchallenge.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.auth.dto.UserLoginRequestDto;
import org.example.nextchallenge.auth.dto.UserLoginResponseDto;
import org.example.nextchallenge.security.JwtTokenProvider;
import org.example.nextchallenge.user.entity.User;
import org.example.nextchallenge.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; //암호화시 사용

    // 로그인 처리
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        //암호화 안하는버전
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

//        //  암호화 버전
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
//        }


        String token = jwtTokenProvider.createToken(
                user.getId(),
                user.getLoginId(),
                user.getRole().name()
        );

            return new UserLoginResponseDto(
                    token,
                user.getUsername(),
                user.getLoginId(),
                user.getRole().name()
        );
    }
}
