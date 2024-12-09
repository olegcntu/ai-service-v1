package org.example.aiservicev3.service.recommendation_service;

import org.example.aiservicev3.proxy.UserServiceProxy;
import org.example.aiservicev3.proxy.ProductServiceProxy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRecommendationRequestHandler {
    private final ChatRecommendationService chatRecommendationService;
    private final RecommendationGeneratorService recommendationGeneratorService;
    private final ProductServiceProxy productServiceProxy;
    private final UserServiceProxy userServiceProxy;

    public ChatRecommendationRequestHandler(ChatRecommendationService chatRecommendationService, RecommendationGeneratorService recommendationGeneratorService, ProductServiceProxy productServiceProxy, UserServiceProxy userServiceProxy) {
        this.chatRecommendationService = chatRecommendationService;
        this.recommendationGeneratorService = recommendationGeneratorService;
        this.productServiceProxy = productServiceProxy;
        this.userServiceProxy = userServiceProxy;
    }

    public Map<String, Object> getResult(String userEmail, String category) {

        Map<String, Object> response = new HashMap<>();
        try {
            if (userEmail == null) {
                response.put("error", "User not specified in request");
                return response;
            }

            String userWithProducts = userServiceProxy.findUserWithProductsByEmail(userEmail);

            if (userWithProducts == null) {
                response.put("error", "User with email " + userEmail + " not found in the system");
                return response;
            }

            String recommendationQueryToAI = recommendationGeneratorService.generateQueryToAI(category, userEmail, userWithProducts);

            String[] recommendations = chatRecommendationService.question(recommendationQueryToAI);

            for (String recommendation : recommendations) {
                if (!productServiceProxy.isProductExists(recommendation)) {
                    throw new Exception();
                }
            }

            if (recommendations.length == 0) {
                throw new Exception();
            }
            response.put("reply", recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Failed to process recommendation request.");
        }
        return response;
    }

}
