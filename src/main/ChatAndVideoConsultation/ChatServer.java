package com.example.project.ChatAndVideoConsultation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ChatServer {
    // list of messages
    private ArrayList<ChatMessage> messages;

    // constructor
    public ChatServer() {
        messages = new ArrayList<>();
    }


    // method to broadcast message to all clients
    public void broadcastMessage(ChatMessage message) {
        messages.add(message);
        System.out.println(message);
    }

    // method to display chat history
    public void displayChatHistory() {
        System.out.println("Chat History:");
        for (ChatMessage message : messages) {
            System.out.println(message);
        }
    }

    // method to get all messages
    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }


    // method to export the chat to the file
    public void exportChat(String patientName, String doctorName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String folderName = "chats";
        new File(folderName).mkdir();

        String fileName = folderName + "/chat-" + patientName + "-" + doctorName + "-" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (ChatMessage msg : messages) {
                writer.write(msg.toString());
                writer.newLine();
            }
            System.out.println("Chat saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving chat: " + e.getMessage());
        }
    }


    // method to import chat from the file
    public void importChat(String fileName) {
        String fullPath = new File("chats", fileName).getAbsolutePath();
        System.out.println("📂 Importing chat from: " + fullPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error importing chat: " + e.getMessage());
        }
    }

    // method to search some keywords in the chat
    public String searchInChat(String filename, String keyword) {
        String fullPath = new File("chats", filename).getAbsolutePath();
        System.out.println("🔍 Searching for '" + keyword + "' in: " + fullPath);
        StringBuilder result = new StringBuilder();
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyword)) {
                    result.append(line).append("\n");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No message found with keyword: " + keyword);
            }
        } catch (IOException e) {
            System.out.println("Error searching in chat: " + e.getMessage());
        }
        return result.toString();
    }

    // method to delete the chat file
    public void deleteChat(String filename) {
        String fullPath = new File("chats", filename).getAbsolutePath();
        System.out.println("🗑️ Deleting chat file: " + fullPath);
        File file = new File(fullPath);
        if (file.delete()) {
            System.out.println("Chat file deleted successfully.");
        } else {
            System.out.println("Failed to delete the chat file.");
        }
    }
    
}
