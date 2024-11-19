package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Advertiseservice;
import com.paisa_square.paisa.service.ChatService;
import com.paisa_square.paisa.service.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Chat> sendMessage(@RequestBody Chat chat) {
        Chat savedChat = chatService.saveMessage(chat);
        return ResponseEntity.ok(savedChat);
    }

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<Chat>> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        List<Chat> messages = chatService.getMessages(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }
}

