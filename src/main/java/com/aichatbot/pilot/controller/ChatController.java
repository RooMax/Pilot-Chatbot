package com.aichatbot.pilot.controller;

import com.aichatbot.pilot.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;


    @PostMapping
    public ResponseEntity<?> chat(@NotNull @RequestBody Map<String, String> payload, HttpSession httpSession) {
        String userMessage = payload.get("message");
        String model = payload.getOrDefault("model", "openai/gpt-3.5-turbo");
        List<Map<String, String>> chatHistory = (List<Map<String, String>>) httpSession.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }
        chatHistory.add(Map.of("role", "user", "content", userMessage));
        String reply = chatService.chat(userMessage, model);
        chatHistory.add(Map.of("role", "assistant", "content", reply));
        httpSession.setAttribute("chatHistory", chatHistory);

//        if (userMessage == null || userMessage.trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("User message is missing.");
//        }


        return ResponseEntity.ok(reply);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetChat(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

}

