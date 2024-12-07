package org.example.aiservicev3.service.question_service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatQuestionService {
    private final ChatClient chatClient;

    public ChatQuestionService(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    public String question(String queryToAI) {

        return chatClient.prompt()
                .user(queryToAI)
                .call()
                .content();
    }
}
