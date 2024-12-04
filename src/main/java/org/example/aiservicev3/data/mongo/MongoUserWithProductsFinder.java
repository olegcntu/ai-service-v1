package org.example.aiservicev3.data.mongo;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MongoUserWithProductsFinder {
    private static final String MONGO_URL = "mongodb://localhost:27017/marketplace";
    private static final String DATABASE_NAME = "marketplace";
    private static final String USERS_COLLECTION = "users";
    private static final String PRODUCTS_COLLECTION = "products";

    public static String findUserWithProductsByEmail(String email) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URL)) {
            MongoCollection<Document> usersCollection = mongoClient.getDatabase(DATABASE_NAME).getCollection(USERS_COLLECTION);
            MongoCollection<Document> productsCollection = mongoClient.getDatabase(DATABASE_NAME).getCollection(PRODUCTS_COLLECTION);

            Document query = new Document("email", email);
            Document user = usersCollection.find(query).first();

            if (user == null) {
                return null;
            }

            List<ObjectId> historyIds = user.containsKey("history") ? user.getList("history", ObjectId.class) : List.of();
            List<ObjectId> wishlistIds = user.containsKey("wishlist") ? user.getList("wishlist", ObjectId.class) : List.of();

            JSONArray historyProducts = getProductsByIds(historyIds, productsCollection);
            JSONArray wishlistProducts = getProductsByIds(wishlistIds, productsCollection);

            JSONObject result = new JSONObject();
            result.put("firstname", user.getString("firstname"));
            result.put("wishlist", wishlistProducts);
            result.put("history", historyProducts);

            return result.toString();
        } catch (MongoTimeoutException e) {
            System.out.println("Failed to connect to MongoDB: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    private static JSONArray getProductsByIds(List<ObjectId> ids, MongoCollection<Document> productsCollection) {
        Set<ObjectId> uniqueIds = new HashSet<>(ids);
        JSONArray productsArray = new JSONArray();

        for (ObjectId productId : uniqueIds) {
            Document productQuery = new Document("_id", productId);
            Document product = productsCollection.find(productQuery).first();

            if (product != null) {
                JSONObject productJson = new JSONObject();
                productJson.put("id", productId.toHexString());
                productJson.put("category", product.getString("category"));
                productJson.put("title", product.getString("title"));
                productJson.put("category", product.getString("category"));
                productJson.put("description", product.getString("description"));
                productsArray.put(productJson);
            }
        }

        return productsArray;
    }

    public static void main(String[] args) {
        String email = "";
        String userWithProducts = findUserWithProductsByEmail(email);

        if (userWithProducts != null) {
            System.out.println(userWithProducts);
        }
    }
}
