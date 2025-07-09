package com.example.project.ChatAndVideoConsultation;

import com.example.project.ChatAndVideoConsultation.*;

public class ChatClient {
    // attributes
    private String name;
    private ChatServer server;

    // constructor
    public ChatClient(String name, ChatServer server) {
        this.name = name;
        this.server = server;
    }

    // method to send mesgage to the server
    public void sendMessage(String messageText) {
        ChatMessage message = new ChatMessage(name, messageText);
        server.broadcastMessage(message);
    }

    // method to receive message from the server
    public void viewAllMessages() {
        System.out.println("Chat History for " + name + ":");
        for (ChatMessage message : server.getMessages()) {
            System.out.println(message);
        }
    }
}
