package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderIdAndReceiverIdOrderByTimestamp(Long senderId, Long receiverId);
    @Query("SELECT DISTINCT c.receiverId FROM Chat c WHERE c.senderId = :userId " +
            "UNION " +
            "SELECT DISTINCT c.senderId FROM Chat c WHERE c.receiverId = :userId")
    List<Long> findChattedUserIds(@Param("userId") Long userId);

    @Query("SELECT c FROM Chat c " +
            "WHERE (c.senderId = :senderId AND c.receiverId = :receiverId) " +
            "OR (c.receiverId = :senderId AND c.senderId = :receiverId) " +
            "ORDER BY c.timestamp ASC")
    List<Chat> findBySenderIdOrReceiverIdOrderByTimestamp(Long senderId, Long receiverId);
}