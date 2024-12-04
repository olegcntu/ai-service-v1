package org.example.aiservicev3.service.question_service;//package org.example.aiservicev2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatQuestionService {
    private final ChatClient chatClient;
    @Autowired
    private QuestionGeneratorService questionGeneratorService;

    public ChatQuestionService(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    public String question(String userEmail, String message) {
        String queryToAI = questionGeneratorService.generateQueryToAI(userEmail, message);

        return chatClient.prompt()
                .user(queryToAI)
                .call()
                .content();
    }
}
