package org.example.nextchallenge.chat.repository;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByLectureIdOrderByCreatedAtAsc(Long lectureId);

    List<ChatMessage> findByLectureIdOrderByCreatedAtDesc(Long lectureId);

    List<ChatMessage> findByLectureIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long lectureId, LocalDateTime cursor);
}
