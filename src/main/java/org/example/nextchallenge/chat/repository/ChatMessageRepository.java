package org.example.nextchallenge.chat.repository;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

//특별한 몽고 디비 interface 지금은 이대로만 알면될듯 -> 자동 함수들이 있음
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByLectureIdOrderByCreatedAtAsc(Long lectureId);
}
