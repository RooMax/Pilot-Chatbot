package com.aichatbot.pilot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userMessage;
    private String botReply;
    private LocalDateTime timestamp  = LocalDateTime.now();

    @PrePersist
    public void onCreate(){
        this.timestamp = LocalDateTime.now();
    }
}
