package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateMessageRequest {

    @NotBlank(message = "Text must not be blank")    
    @Size(max = 50, message = "Text must be less than or equal to 50 characters")
    private String text;

    public CreateMessageRequest() {
    }

    public CreateMessageRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
