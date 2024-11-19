package com.paisa_square.paisa.service;
import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Chat;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.ChatRepository;
import com.paisa_square.paisa.repository.Commentrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> getMessages(Long senderId, Long receiverId) {
        return chatRepository.findBySenderIdAndReceiverIdOrderByTimestamp(senderId, receiverId);
    }

    public Chat saveMessage(Chat chat) {
        return chatRepository.save(chat);
    }
}