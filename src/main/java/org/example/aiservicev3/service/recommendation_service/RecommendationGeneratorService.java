package org.example.aiservicev3.service.recommendation_service;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RecommendationGeneratorService {


    public String generateQueryToAI(String category, String userEmail, String dbInfo) {

        String additionalInfoAboutCategory = "";
        if (!Objects.equals(category, "")) {
            additionalInfoAboutCategory = "The user is currently viewing the category " + category +"(even if he has never viewed products in this category, you must answer)";
        }
        String additionalInfoField = additionalInfoAboutCategory.isEmpty() ? "" :
                String.format("\"additional_info\": \"%s\",", additionalInfoAboutCategory);


        String context = String.format("{\"role\": \"You are a recommendation system for an online store..\"," +
                        " %s \"instruction\": \"Based on the provided data about the user’s wishlist and history, create a list of recommendations.\"," +
                        " \"rules\": \"The response should only contain an array of recommended product IDs. Do not add backticks, spaces outside the array, or any other characters.Products must be no more than 4. Sample answer: [\\\"id1\\\", \\\"id2\\\", \\\"id3\\\", \\\"id4\\\"]\"," +
                        " \"user_data\": {\"wishlist and history\": %s}}",
                additionalInfoField, dbInfo);


        return context;
    }


}
