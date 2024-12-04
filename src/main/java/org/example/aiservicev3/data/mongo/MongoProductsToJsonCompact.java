package org.example.aiservicev3.data.mongo;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class MongoProductsToJsonCompact {
    public static String read() {
        String MONGO_URL = "mongodb://localhost:27017/marketplace";
        String DATABASE_NAME = "marketplace";
        String COLLECTION_NAME = "products";

        try (MongoClient mongoClient = MongoClients.create(MONGO_URL)) {
            MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);

            JSONArray jsonArray = new JSONArray();

            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", doc.getString("title"));
                    jsonObject.put("description", doc.getString("description").replace("\\", ""));
                    jsonObject.put("price", doc.getInteger("price", 0));

                    jsonArray.put(jsonObject);
                }
            }
            return jsonArray.toString();

        } catch (MongoTimeoutException e) {
            System.out.println("Failed to connect to MongoDB: " + e.getMessage());
            return "{}";
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return "{}";
        }
    }
}
