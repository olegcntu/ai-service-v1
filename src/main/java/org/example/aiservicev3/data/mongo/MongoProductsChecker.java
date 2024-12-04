package org.example.aiservicev3.data.mongo;


import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoProductsChecker {
    private static final String MONGO_URL = "mongodb://localhost:27017/marketplace";
    private static final String DATABASE_NAME = "marketplace";
    private static final String PRODUCTS_COLLECTION = "products";

    public static boolean areProductsExist(String[] productArr) {
        List<String> productIds = List.of(productArr);

        try (MongoClient mongoClient = MongoClients.create(MONGO_URL)) {
            MongoCollection<Document> productsCollection = mongoClient.getDatabase(DATABASE_NAME).getCollection(PRODUCTS_COLLECTION);

            for (String id : productIds) {
                try {
                    ObjectId objectId = new ObjectId(id);
                    Document productQuery = new Document("_id", objectId);
                    Document product = productsCollection.find(productQuery).first();

                    if (product == null) {
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid ID: " + id);
                    return false;
                }
            }

            return true;
        } catch (MongoTimeoutException e) {
            System.out.println("Failed to connect to MongoDB: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String arr[] = {"649403ec58504419126eaf3a", "649403ec58504419126eaf3a"};

        boolean result = areProductsExist(arr);
        System.out.println("All products exist: " + result);
    }
}