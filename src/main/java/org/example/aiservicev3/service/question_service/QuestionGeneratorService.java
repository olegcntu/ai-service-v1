package org.example.aiservicev3.service.question_service;

import org.example.aiservicev3.data.mongo.MongoProductsToJsonCompact;
import org.example.aiservicev3.data.postgres.MessageService;
import org.example.aiservicev3.entity.MessageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Service
public class QuestionGeneratorService {

    @Autowired
    private final MessageService messageService;

    public QuestionGeneratorService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String generateQueryToAI(String userEmail, String clientQuery) {
        String dbInfo = MongoProductsToJsonCompact.read();
        Optional<MessageRecord> latestMessage = messageService.findLatestMessageByEmail(userEmail);
        String st = latestMessage
                .map(record -> "\"Last message - використовуй це якщо потрібно, та не звертай уваги на попереднє повідомлення, якщо вважаєш його не потрібним\": \"" + record.getMessage() + ", Timestamp: " + record.getTimestamp()+"\"")
                .orElse("");

        String context = String.format("{\"role\": \"Ти консультант в інтернет-магазині, який допомагає клієнтам.\"," +
                " \"instruction\": \"Дай відповідь так, щоб клієнту було зрозуміло і він зміг прийняти рішення.\"," +
                "%s \"db_info\": \"У магазині є %s.\", \"client_query\": \"%s\"}",st, dbInfo, clientQuery);

        System.out.println(context);

        messageService.saveMessage(userEmail, clientQuery);
        return context;

    }


    public static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        return content.toString();
    }
}
