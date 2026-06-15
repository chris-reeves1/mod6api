package com.example.demo.controller;

import com.example.demo.dto.CreateMessageRequest;
import com.example.demo.dto.MessageDto;
import com.example.demo.service.MessageService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping
    public List<MessageDto> getAllMessages() {
        return service.getAllMessages();
    }

    @PostMapping
    public MessageDto createMessage(@RequestBody MessageDto dto) {
        return service.createMessage(dto);
    }

    @GetMapping("/{id}")
    public MessageDto getMessageById(@PathVariable Long id) {
        return service.getMessageById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        service.deleteMessage(id);
}

    @PutMapping("/{id}")
    public MessageDto updateMessage(@PathVariable Long id, @RequestBody CreateMessageRequest request) {
        return service.updateMessage(id, request);
    }
}
