package org.example.aiservicev3.controllers;//package org.example.aiservicev2.controllers;


import org.example.aiservicev3.data.mongo.MongoProductsChecker;
import org.example.aiservicev3.service.question_service.ChatQuestionService;
import org.example.aiservicev3.service.recommendation_service.ChatRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    ChatQuestionService chatQuestionService;

    @Autowired
    ChatRecommendationService chatRecommendationService;

    @PostMapping("/user-and-question")
    public ResponseEntity<Map<String, String>> questionRequest(@RequestBody Map<String, String> requestBody) {
        String userMessage = requestBody.get("message");
        String userEmail = requestBody.get("email");

        Map<String, String> response = new HashMap<>();
        String result = chatQuestionService.question(userEmail, userMessage);
        response.put("reply", result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user-and-recommendation")
    public ResponseEntity<Map<String, Object>> recommendationRequest(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("email");
        String category = requestBody.get("category");

        Map<String, Object> response = new HashMap<>();
        try {
            String[] recommendations = chatRecommendationService.question(category, userEmail);
            if (!MongoProductsChecker.areProductsExist(recommendations)) {
                throw new Exception();
            }
            if (recommendations.length == 0) {
                throw new Exception();
            }
            response.put("reply", recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Failed to process recommendation request.");
        }

        return ResponseEntity.ok(response);
    }
}
