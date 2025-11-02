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

    private Long lectureId; //mysql
    private Long userId; //mysql-pk

    private String senderLoginId; //users.login_id(학번)
    private String senderUsername; //users.username(이름)
    private String role; //학생 or 교수

    private String content;

    private LocalDateTime createdAt;
}
