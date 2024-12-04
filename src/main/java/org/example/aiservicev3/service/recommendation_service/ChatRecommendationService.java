package org.example.aiservicev3.service.recommendation_service;//package org.example.aiservicev2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aiservicev3.service.question_service.QuestionGeneratorService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRecommendationService {
    private final ChatClient chatClient;
    @Autowired
    private RecommendationGeneratorService recommendationGeneratorService;

    public ChatRecommendationService(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    public String[] question(String category, String userEmail) {

        try {
            String recommendationQueryToAI = recommendationGeneratorService.generateQueryToAI(category, userEmail);
            String response = chatClient.prompt()
                    .user(recommendationQueryToAI)
                    .call()
                    .content();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, String[].class); // Парсинг строки в массив
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
