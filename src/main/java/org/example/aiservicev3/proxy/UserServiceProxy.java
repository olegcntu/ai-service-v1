package org.example.aiservicev3.proxy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class UserServiceProxy {

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public String findUserWithProductsByEmail(String email) {
        try {
            String userEndpoint = userServiceUrl + "get-by-email/" + email;
            JSONObject userResponse = getJsonFromEndpoint(userEndpoint);

            if (userResponse == null || !userResponse.has("user")) {
                return null;
            }

            JSONObject user = userResponse.getJSONObject("user");

            List<String> historyIds = jsonArrayToList(user.optJSONArray("history"));
            List<String> wishlistIds = jsonArrayToList(user.optJSONArray("wishlist"));

            JSONArray historyProducts = getProductsWithCount(historyIds);
            JSONArray wishlistProducts = getProductsWithCount(wishlistIds);

            JSONObject result = new JSONObject();
            result.put("firstname", user.optString("firstname"));
            result.put("wishlist", wishlistProducts);
            result.put("history", historyProducts);

            return result.toString();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    private JSONObject getJsonFromEndpoint(String endpoint) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        connection.disconnect();
        return new JSONObject(response.toString());
    }

    private JSONArray getProductsWithCount(List<String> ids) {
        Map<String, Integer> idCounts = new HashMap<>();

        for (String id : ids) {
            idCounts.put(id, idCounts.getOrDefault(id, 0) + 1);
        }

        JSONArray products = new JSONArray();

        for (Map.Entry<String, Integer> entry : idCounts.entrySet()) {
            String id = entry.getKey();
            int count = entry.getValue();

            try {
                String productEndpoint = productServiceUrl + id;
                JSONObject product = getJsonFromEndpoint(productEndpoint);

                if (product != null) {
                    JSONObject productSummary = new JSONObject();
                    productSummary.put("id", product.optString("_id"));
                    productSummary.put("title", product.optString("title"));
                    productSummary.put("description", product.optString("description"));
                    productSummary.put("category", product.optString("category"));
                    productSummary.put("visitCount", count); // Добавлено поле с количеством посещений

                    products.put(productSummary);
                }
            } catch (Exception e) {

            }
        }

        return products;
    }

    private List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        }

        return list;
    }
}
