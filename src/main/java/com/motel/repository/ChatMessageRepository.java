package com.motel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.motel.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
    List<ChatMessage> findAllBySenderIdOrRecipientId(String senderId, String recipientId);
}
