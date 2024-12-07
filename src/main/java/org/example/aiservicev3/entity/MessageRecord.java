package org.example.aiservicev3.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MessageRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024)
    private String message;
    @Column(length = 2048)
    private String response;
    @Column(length = 2048)
    private String recommendation;
    @Column(length = 512)
    private String email;

    private LocalDateTime timestamp;

    public MessageRecord() {
    }

    public MessageRecord(String message, String response, String recommendation, String email, LocalDateTime timestamp) {
        this.message = message;
        this.email = email;
        this.timestamp = timestamp;
        this.response = response;
        this.recommendation = recommendation;
    }

}

