package com.example.project.ChatAndVideoConsultation;

// import Doctor_Patient_Interaction.*;

import java.io.File;
import java.util.Scanner;

public class ChatTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChatServer server = new ChatServer();

        while (true) {
            System.out.println("\n========= 💬 CHAT SYSTEM MENU =========");
            System.out.println("1. Start New Chat");
            System.out.println("2. View Existing Chat (Import)");
            System.out.println("3. Search in Chat File");
            System.out.println("4. Export Chat");
            System.out.println("5. Delete Chat File");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    startNewChat(scanner, server);
                    break;
                case "2":
                    showAvailableFiles();
                    System.out.print("📂 Enter filename to import: ");
                    String fileToImport = scanner.nextLine();
                    server.importChat(fileToImport);
                    break;
                case "3":
                    showAvailableFiles();
                    System.out.print("🔍 Enter filename to search in: ");
                    String fileToSearch = scanner.nextLine();
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    System.out.println(server.searchInChat(fileToSearch, keyword));
                    break;
                case "4":
                    System.out.print("👨‍⚕️ Doctor's name: ");
                    String doc = scanner.nextLine();
                    System.out.print("🧑‍💼 Patient's name: ");
                    String pat = scanner.nextLine();
                    server.exportChat(pat, doc);
                    break;

                case "5":
                    showAvailableFiles();
                    System.out.print("🗑️ Enter filename to delete: ");
                    String fileToDelete = scanner.nextLine();
                    File file = new File("chats/" + fileToDelete);
                    if (file.delete()) {
                        System.out.println("✅ File deleted successfully.");
                    } else {
                        System.out.println("❌ Failed to delete the file. It may not exist.");
                    }
                    break;
                case "6":
                    System.out.println("👋 Exiting Chat System. Bye!");
                    return;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void startNewChat(Scanner scanner, ChatServer server) {
        System.out.print("👨‍⚕️ Enter Doctor's name: ");
        String doctorName = scanner.nextLine();
        ChatClient doctor = new ChatClient(doctorName, server);

        System.out.print("🧑‍💼 Enter Patient's name: ");
        String patientName = scanner.nextLine();
        ChatClient patient = new ChatClient(patientName, server);

        System.out.println("\n💬 Start chatting! Type `exit` to end.\n");

        boolean chatting = true;
        boolean doctorTurn = false;

        while (chatting) {
            if (doctorTurn) {
                System.out.print("👨‍⚕️ " + doctorName + ": ");
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("exit")) break;
                doctor.sendMessage(msg);
                if (msg.equalsIgnoreCase("/video")) {
                    VideoCall vc = new VideoCall(doctorName, patientName);
                    vc.startCall();
                    continue;
                }
            } else {
                System.out.print("🧑‍💼 " + patientName + ": ");
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("exit")) break;
                patient.sendMessage(msg);
                if (msg.equalsIgnoreCase("/video")) {
                    VideoCall vc = new VideoCall(doctorName, patientName);
                    vc.startCall();
                    continue;
                }
            }
            doctorTurn = !doctorTurn;
        }

        System.out.println("\n📃 Final Chat Transcript:");
        doctor.viewAllMessages();

        // 🔥 Auto-export
        server.exportChat(patientName, doctorName);
    }

    private static void showAvailableFiles() {
        File dir = new File("chats");
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));

        System.out.println("\n📂 Available Chat Files:");
        if (files != null && files.length > 0) {
            for (File file : files) {
                System.out.println("• " + file.getName());
            }
        } else {
            System.out.println("❌ No chat files found in /chats folder.");
        }
    }

}
