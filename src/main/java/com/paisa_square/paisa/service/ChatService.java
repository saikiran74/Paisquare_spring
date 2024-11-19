package com.paisa_square.paisa.service;
import com.paisa_square.paisa.DTO.ChatHistoryUsersDTO;
import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Chat;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.User;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.ChatRepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Chat> getMessages(Long senderId, Long receiverId) {
        return chatRepository.findBySenderIdOrReceiverIdOrderByTimestamp(senderId, receiverId);
    }

    public Chat saveMessage(Chat chat) {
        return chatRepository.save(chat);
    }

    public List<ChatHistoryUsersDTO> getChatHistoryUsers(Long userId) {
        List<Long> userIds = chatRepository.findChattedUserIds(userId);
        return userRepository.findAllById(userIds).stream().map(user -> {
            return new ChatHistoryUsersDTO(user.getId(), user.getUsername());
        }).toList();
    }

}