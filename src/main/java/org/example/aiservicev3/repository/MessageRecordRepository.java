package org.example.aiservicev3.repository;


import org.example.aiservicev3.entity.MessageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRecordRepository extends JpaRepository<MessageRecord, Long> {

    @Query("SELECT m FROM MessageRecord m WHERE m.email = :email ORDER BY m.timestamp DESC")
    List<MessageRecord> findAllByEmailOrderByTimestampDesc(@Param("email") String email);

    List<MessageRecord> findAllByOrderByTimestampDesc();
}

