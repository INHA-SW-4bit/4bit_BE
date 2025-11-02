package org.example.nextchallenge.user.details;

import lombok.Getter;
import org.example.nextchallenge.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한이 따로 없다면 빈 리스트 리턴
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 패스워드 필드명이 다르면 맞게 수정
    }

    @Override
    public String getUsername() {
        // username은 JWT에서 식별자로 쓰는 필드 (ex. email, 학번)
        return user.getUsername();
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

    // userId도 바로 꺼낼 수 있게 getter 추가
    public Long getUserId() {
        return user.getId();
    }
}
