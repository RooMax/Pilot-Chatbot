package com.aichatbot.pilot.service;

import com.aichatbot.pilot.model.ChatLog;
import com.aichatbot.pilot.repo.ChatLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ChatLogRepository chatLogRepository;

    public ChatService(ChatLogRepository chatLogRepository) {
        this.chatLogRepository = chatLogRepository;
    }

    public String chat(String userMessage, String model) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "https://localhost");
        headers.set("X-Title", "AI Chatbot");

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", userMessage))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

            // Debug log
            System.out.println("OpenRouter Response (chat): " + response.getBody());

            if (response.getBody() == null || !response.getBody().containsKey("choices")) {
                return "OpenRouter returned an unexpected response.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) {
                return "OpenRouter did not return a valid reply.";
            }

            String reply = ((Map<String, String>) choices.get(0).get("message")).get("content");

            chatLogRepository.save(ChatLog.builder()
                    .userMessage(userMessage)
                    .botReply(reply)
                    .build());

            return reply;

        } catch (HttpClientErrorException.Unauthorized e) {
            return "Unauthorized: Check your API key.";
        } catch (HttpClientErrorException.TooManyRequests e) {
            return "Rate limit exceeded. Please try again later.";
        } catch (HttpStatusCodeException e) {
            return "OpenRouter error: " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    public String chatWithHistory(List<Map<String, String>> chatHistory, String model) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "https://localhost");
        headers.set("X-Title", "AI Chatbot");

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", chatHistory
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

            // Debug log
            System.out.println("OpenRouter Response (chatWithHistory): " + response.getBody());

            if (response.getBody() == null || !response.getBody().containsKey("choices")) {
                return "OpenRouter returned an unexpected response.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) {
                return "OpenRouter did not return a valid reply.";
            }

            String reply = ((Map<String, String>) choices.get(0).get("message")).get("content");

            // Save last user message & bot reply
            String lastUserMessage = chatHistory.get(chatHistory.size() - 1).get("content");
            chatLogRepository.save(ChatLog.builder()
                    .userMessage(lastUserMessage)
                    .botReply(reply)
                    .build());

            return reply;

        } catch (HttpStatusCodeException e) {
            return "OpenRouter error: " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }
}
