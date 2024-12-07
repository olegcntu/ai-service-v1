package org.example.aiservicev3.controllers;

import org.example.aiservicev3.service.question_service.ChatRequestHandler;
import org.example.aiservicev3.service.recommendation_service.ChatRecommendationRequestHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {

    private final ChatRecommendationRequestHandler chatRecommendationRequestHandler;
    private final ChatRequestHandler chatRequestHandler;

    public RequestController(ChatRecommendationRequestHandler chatRecommendationRequestHandler, ChatRequestHandler chatRequestHandler) {
        this.chatRecommendationRequestHandler = chatRecommendationRequestHandler;
        this.chatRequestHandler = chatRequestHandler;
    }

    @PostMapping("/user-and-question")
    public ResponseEntity<Map<String, Object>> questionRequest(@RequestBody Map<String, String> requestBody) {
        String userMessage = requestBody.get("message");
        String userEmail = requestBody.get("email");

        Map<String, Object> response = chatRequestHandler.getResult(userEmail, userMessage);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user-and-recommendation")
    public ResponseEntity<Map<String, Object>> recommendationRequest(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("email");
        String category = requestBody.get("category");

        Map<String, Object> response = chatRecommendationRequestHandler.getResult(userEmail, category);
        return ResponseEntity.ok(response);
    }
}
