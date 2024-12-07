package org.example.aiservicev3.service.question_service.model;

import java.util.Map;

public class ResultObject {
    private String message;
    private Map<String, String> extractedMap;

    public ResultObject(String message, Map<String, String> extractedMap) {
        this.message = message;
        this.extractedMap = extractedMap;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getExtractedMap() {
        return extractedMap;
    }
}
