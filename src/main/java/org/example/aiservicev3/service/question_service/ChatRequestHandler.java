package org.example.aiservicev3.service.question_service;

import org.example.aiservicev3.proxy.ProductServiceProxy;
import org.example.aiservicev3.service.MessageService;
import org.example.aiservicev3.entity.MessageRecord;
import org.example.aiservicev3.service.question_service.model.ResultObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatRequestHandler {
    private final ChatQuestionService chatQuestionService;
    private final QuestionGeneratorService questionGeneratorService;
    private final MessageService messageService;
    private final ProductServiceProxy productServiceProxy;


    public ChatRequestHandler(ChatQuestionService chatQuestionService, QuestionGeneratorService questionGeneratorService, MessageService messageService, ProductServiceProxy productServiceProxy) {
        this.chatQuestionService = chatQuestionService;
        this.questionGeneratorService = questionGeneratorService;
        this.messageService = messageService;
        this.productServiceProxy = productServiceProxy;
    }

    public Map<String, Object> getResult(String userEmail, String userMessage) {
        String products = productServiceProxy.getProducts();
        String lastMessage = getLastMessage(userEmail);

        String queryToAI = questionGeneratorService.generateQueryToAI(products, lastMessage, userMessage);
        String chatResult = chatQuestionService.question(queryToAI);

        ResultObject res = processInput(chatResult);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> productsWithName = new ArrayList<>();


        for (Map.Entry<String, String> entry : res.getExtractedMap().entrySet()) {
            String productName = entry.getKey();
            String productId = entry.getValue();
            if (productServiceProxy.isProductExists(productId)) {
                productsWithName.add(Map.of("name", productName, "link", "product/" + productId));
            }
        }


        String recommendation = convertMapToJsonString(res.getExtractedMap());

        messageService.saveMessage(userMessage, res.getMessage(), recommendation, userEmail);
        response.put("reply", res.getMessage());
        response.put("products", productsWithName);

        return response;
    }


    private String getLastMessage(String userEmail) {
        String lastMessage = "";
        if (userEmail != null && !userEmail.isEmpty()) {
            Optional<MessageRecord> latestMessage = messageService.findLatestMessageByEmail(userEmail);
            lastMessage = latestMessage
                    .map(record -> "\"Last message - використовуй це якщо потрібно, та не звертай уваги на попереднє повідомлення, якщо вважаєш його не потрібним\": \"" + record.getMessage() + ", Timestamp: " + record.getTimestamp() + "\"")
                    .orElse("");
        }
        return lastMessage;
    }

    private ResultObject processInput(String input) {
        String cleanedMessage = input.replaceAll("\\*", "").replaceAll("```", "").replaceAll("json", "").trim();

        Map<String, String> extractedMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String jsonSection = matcher.group(1).trim();
            // Attempt to parse key-value pairs in the JSON-like section
            String[] entries = jsonSection.split(",");
            for (String entry : entries) {
                String[] keyValue = entry.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim().replaceAll("\"", "");
                    extractedMap.put(key, value);
                }
            }
        }

        cleanedMessage = cleanedMessage.replaceAll("\\{.*?\\}", "").trim();
        cleanedMessage = cleanedMessage.contains("{") ? cleanedMessage.substring(0, cleanedMessage.indexOf("{")).trim() : cleanedMessage;

        return new ResultObject(cleanedMessage, extractedMap);
    }

    public static String convertMapToJsonString(Map<String, String> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        int count = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonBuilder.append("\"").append(entry.getKey()).append("\":");
            jsonBuilder.append("\"").append(entry.getValue()).append("\"");

            if (count < map.size() - 1) {
                jsonBuilder.append(",");
            }
            count++;
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

}
