package org.example.aiservicev3.data.postgres;

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

    public void saveMessage(String email, String message) {
        MessageRecord record = new MessageRecord(message, email, LocalDateTime.now());
        repository.save(record);
    }

    public Optional<MessageRecord> findLatestMessageByEmail(String email) {
        List<MessageRecord> records = repository.findAllByEmailOrderByTimestampDesc(email);
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }

}
