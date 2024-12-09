package org.example.aiservicev3.service;

import org.example.aiservicev3.repository.MessageRecordRepository;
import org.example.aiservicev3.entity.MessageRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRecordRepository repository;

    public MessageService(MessageRecordRepository repository) {
        this.repository = repository;
    }

    public void saveMessage(String message, String response,String recommendation, String email) {
        MessageRecord record = new MessageRecord(message, response,recommendation, email, LocalDateTime.now());
        repository.save(record);
    }

    public Optional<MessageRecord> findLatestMessageByEmail(String email) {
        List<MessageRecord> records = repository.findAllByEmailOrderByTimestampDesc(email);
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }

    public List<MessageRecord> findAllMessagesSortedByTime() {
        return repository.findAllByOrderByTimestampDesc();
    }
    public List<MessageRecord> findAllMessagesByEmailSortedByTime(String email) {
        return repository.findAllByEmailOrderByTimestampDesc(email);
    }
}

