package com.motel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.motel.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
    List<ChatMessage> findAllBySenderIdOrRecipientId(String senderId, String recipientId);
    
    @Query("{ '$or': [ { 'senderId': { '$ne': ?0 } }, { 'recipientId': { '$ne': ?0 } } ] }")
    List<ChatMessage> findBySenderIdOrRecipientId(String id);
    
    @Query("{ 'senderId': ?0 }")
    List<ChatMessage> findRecipientIdsBySenderId(String senderId);
    
    List<ChatMessage> findBySenderId(String senderId);

    @Query("{'recipientId': ?0}")
    List<String> findDistinctSenderIdByRecipientId(String recipientId);

    @Query("{'senderId': ?0}")
    List<String> findDistinctRecipientIdBySenderId(String senderId);
}
