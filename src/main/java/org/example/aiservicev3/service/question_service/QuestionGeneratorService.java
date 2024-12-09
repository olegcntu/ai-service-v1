package org.example.aiservicev3.service.question_service;

import org.example.aiservicev3.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class QuestionGeneratorService {

    @Autowired
    private final MessageService messageService;

    public QuestionGeneratorService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String generateQueryToAI(String products, String latestMessage, String clientQuery) {

        String context = String.format("{\"role\": \"Ти консультант в інтернет-магазині, який допомагає клієнтам. \"," +
                " \"instruction\": \"Дай відповідь так, щоб клієнту було зрозуміло і він зміг прийняти рішення.\"," +
                " \"additional\": \"якщо ти рекомендуєш продукти то в кінці повідомлення в форматі JSON допиши назву продукту на його id (приклад відповіді: your answer {\"name1\": \"link1\",\"name2\": \"link2\" } ), якщо рекомендацій нема - то нічого не одавай\"," +
                "%s \"db_info\": \"У магазині є %s.\", \"client_query\": \"%s\"}", latestMessage, products, clientQuery);


        return context;

    }
}
