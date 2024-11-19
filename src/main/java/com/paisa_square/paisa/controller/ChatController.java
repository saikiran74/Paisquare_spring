package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.DTO.ChatHistoryUsersDTO;
import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.ChatRepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Advertiseservice;
import com.paisa_square.paisa.service.ChatService;
import com.paisa_square.paisa.service.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;

    @PostMapping("/send")
    public ResponseEntity<Chat> sendMessage(@RequestBody Chat chat) {
        Chat savedChat = chatService.saveMessage(chat);
        return ResponseEntity.ok(savedChat);
    }

    @GetMapping("getmessages/{senderId}/{receiverId}")
    public ResponseEntity<List<Chat>> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        List<Chat> messages = chatService.getMessages(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("getchathistoryusers/{userId}")
    public List<ChatHistoryUsersDTO> getChatHistoryUsers(@PathVariable Long userId) {
        List<ChatHistoryUsersDTO> chatHistoryUsers = chatService.getChatHistoryUsers(userId);
        System.out.println("chatHistoryUsers-> "+chatHistoryUsers);
        if (chatHistoryUsers.isEmpty()) {
            // Optionally, initialize an empty conversation if required
            return new ArrayList<>();
        }
        return chatHistoryUsers;
    }

    @PostMapping("/initialize-chat")
    public ResponseEntity<Chat> initializeChat(@RequestBody Chat chat) {
        if (chat.getSenderId() != null && chat.getReceiverId() != null) {
            chat.setTimestamp(LocalDateTime.now());
            chatRepository.save(chat);
            return ResponseEntity.ok(chat);
        }
        return ResponseEntity.badRequest().build();
    }

}



