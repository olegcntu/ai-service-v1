package org.example.aiservicev3.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MessageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String email;

    private LocalDateTime timestamp;

    public MessageRecord() {}

    public MessageRecord(String message, String email, LocalDateTime timestamp) {
        this.message = message;
        this.email = email;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

