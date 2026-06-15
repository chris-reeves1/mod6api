package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.validation.MessageTextValidator;
import com.example.demo.dto.CreateMessageRequest;
import com.example.demo.dto.MessageDto;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final MessageTextValidator validator;

    public MessageService(MessageRepository repository, MessageTextValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<MessageDto> getAllMessages() {
        return repository.findAll().stream()
                .map(this::ToDto)
                .toList();
    }

    public MessageDto createMessage(MessageDto dto) {
        String cleanedText = validateAndClean(dto.getText());
        Message message = new Message(cleanedText);
        Message saved = repository.save(message);   
        return ToDto(saved);
    }

    public MessageDto getMessageById(Long id) {
        Message message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found for id: " + id));
        return ToDto(message);
    }

    public void deleteMessage(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public MessageDto ToDto (Message message) {
        return new MessageDto(message.getId(), message.getText());
    }

    public MessageDto updateMessage(Long id, CreateMessageRequest request) {
        Message message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found for id: " + id));
        String cleanedText = validateAndClean(request.getText());
        message.setText(cleanedText);
        Message saved = repository.save(message);
        return ToDto(saved);
    }

    private String validateAndClean(String text) {
        try {
            validator.validate(text);
            return validator.clean(text);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage()
            );
        }
    }

}
