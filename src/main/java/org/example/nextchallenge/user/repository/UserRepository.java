package org.example.nextchallenge.user.repository;

import org.example.nextchallenge.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // login_id 로 사용자 조회 (JWT 인증 시 사용)
    Optional<User> findByLoginId(String loginId);
}
