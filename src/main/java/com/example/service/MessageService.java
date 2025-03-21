package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> findByAccount(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public Message findById(Integer messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.orElse(null);
    }

    public void deleteMessage(Integer messageId) {
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Integer messageId, String newMessage) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setMessageText(newMessage);
            return messageRepository.save(message);
        }
        return null;
    }
}
