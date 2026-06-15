package com.example.demo.validation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageTextValidator {

    private static final List<String> BANNED_TERMS = List.of(
            "password",
            "admin",
            "root",
            "hack"
    );

    public String clean(String text) {
        return text.trim();
    }

    public void validate(String text) {
        String cleaned = clean(text);
        String lowerCaseText = cleaned.toLowerCase();

        if (containsUrl(lowerCaseText)) {
            throw new IllegalArgumentException("Message must not contain URLs");
        }

        if (containsScriptLikeInput(lowerCaseText)) {
            throw new IllegalArgumentException("Message must not contain script-like input");
        }

        if (containsBannedTerm(lowerCaseText)) {
            throw new IllegalArgumentException("Message contains a banned term");
        }

        if (isAggressiveShouting(cleaned)) {
            throw new IllegalArgumentException("Message must not be written entirely in capitals");
        }
    }

    private boolean containsUrl(String text) {
        return text.contains("http://") || text.contains("https://") || text.contains("www.");
    }

    private boolean containsScriptLikeInput(String text) {
        return text.contains("<script") || text.contains("</script>");
    }

    private boolean containsBannedTerm(String text) {
        return BANNED_TERMS.stream()
                .anyMatch(text::contains);
    }

    private boolean isAggressiveShouting(String text) {
        return text.length() > 10 && text.equals(text.toUpperCase()) && text.matches(".*[A-Z].*");
    }
}
