package com.example.project.ChatAndVideoConsultation;

import java.time.LocalDateTime;

public class ChatMessage {
    // attributes
    private String sender;
    private String message;
    private LocalDateTime timestamp;

    // constructor
    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


    // getters
    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // override toString to provide a formatted string representation of the ChatMessage object
    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender + ": " + message;
    }

}
