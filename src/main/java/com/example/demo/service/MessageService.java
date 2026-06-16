package com.example.demo.service;

import com.example.demo.dto.CreateMessageRequest;
import com.example.demo.dto.MessageDto;
import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.validation.MessageTextValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public MessageDto getMessageById(Long id) {
        Message message = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + id
                ));

        return toDto(message);
    }

    public MessageDto createMessage(CreateMessageRequest request) {
        String cleanedText = validateAndClean(request.getText());

        Message message = new Message(cleanedText);
        Message saved = repository.save(message);

        return toDto(saved);
    }

    public MessageDto updateMessage(Long id, CreateMessageRequest request) {
        Message message = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + id
                ));

        String cleanedText = validateAndClean(request.getText());

        message.setText(cleanedText);

        Message saved = repository.save(message);

        return toDto(saved);
    }

    public void deleteMessage(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Message not found with id: " + id
            );
        }

        repository.deleteById(id);
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

    private MessageDto toDto(Message message) {
        return new MessageDto(message.getId(), message.getText());
    }
}