package org.example.nextchallenge.chat.repository;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    // 기본 전체 조회 (기존 유지)
    List<ChatMessage> findByLectureIdOrderByCreatedAtAsc(Long lectureId);

    // 최신순 전체 조회 (커서 없을 때)
    List<ChatMessage> findByLectureIdOrderByCreatedAtDesc(Long lectureId);

    // 커서 기반 조회 (cursor 이전 데이터만 최신순으로 조회)
    @Query(value = "{ 'lectureId': ?0, 'createdAt': { $lt: ?1 } }", sort = "{ 'createdAt': -1 }")
    List<ChatMessage> findByLectureIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long lectureId, LocalDateTime cursor);
}
