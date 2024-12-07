package org.example.aiservicev3.proxy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ProductServiceProxy {
    @Value("${product.service.url}")
    private String productServiceUrl;

    public String getProducts() {

        try {
            URL url = new URL(productServiceUrl);
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

            JSONArray jsonArray = new JSONArray(response.toString());
            JSONArray resultArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = jsonArray.getJSONObject(i);

                JSONObject formattedProduct = new JSONObject();
                formattedProduct.put("id", product.getString("_id"));
                formattedProduct.put("title", product.getString("title"));
                formattedProduct.put("description", product.getString("description").replace("\\", ""));
                formattedProduct.put("price", product.optInt("price", 0));

                resultArray.put(formattedProduct);
            }

            return resultArray.toString();

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return "{}";
        }
    }

    public  boolean isProductExists(String productId) {
        String SERVICE_URL = productServiceUrl + productId;

        try {
            URL url = new URL(SERVICE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                connection.disconnect();

                // Check if the response contains a valid product JSON
                JSONObject product = new JSONObject(response.toString());
                return product.has("_id") && product.getString("_id").equals(productId);
            } else {
                connection.disconnect();
                return false;
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
}

