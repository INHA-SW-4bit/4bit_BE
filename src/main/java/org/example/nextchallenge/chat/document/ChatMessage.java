package org.example.nextchallenge.chat.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private Long lectureId;       // mysql lecture_id
    private Long userId;          // mysql user pk

    private String senderLoginId; // users.login_id (학번)
    private String senderUsername; // users.username (이름)
    private String role;          // 학생 or 교수

    private String content;       // 채팅 내용
    private LocalDateTime createdAt; // 생성 시각

    //학생 익명 번호 (ex. 익명1, 익명2)
    private Integer anonymousNumber;
}
