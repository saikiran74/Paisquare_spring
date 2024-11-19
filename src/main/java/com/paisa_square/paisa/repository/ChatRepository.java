package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderIdAndReceiverIdOrderByTimestamp(Long senderId, Long receiverId);
}