package com.st10547020.poe;

import java.util.Scanner;

/**
 * Entry point of the application. Handles user registration, login,
 * and the QuickChat messaging menu.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("Enter your username, password, and cell phone number to register.");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Cell phone number: ");
        String cellPhoneNumber = scanner.nextLine();
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.println(login.registerUser(username, password, cellPhoneNumber,
                firstName, lastName));

        System.out.println();
        System.out.println("Please log in.");
        System.out.print("Username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus());

        if (!loggedIn) {
            scanner.close();
            return;
        }

        Message.setCurrentSender(loginUsername);

        Message messageService = new Message();
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        int choice = -1;
        while (choice != 4) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.print("How many messages would you like to send? ");
                int numMessages = Integer.parseInt(scanner.nextLine());

                for (int i = 0; i < numMessages; i++) {
                    String messageID = messageService.generateMessageID();

                    System.out.print("Recipient cell number: ");
                    String recipient = scanner.nextLine();
                    System.out.println(messageService.checkRecipientCell(recipient));

                    System.out.print("Message: ");
                    String messageText = scanner.nextLine();
                    System.out.println(messageService.checkMessageLength(messageText));

                    String hash = messageService.createMessageHash(messageID, i, messageText);

                    System.out.println("Message ID generated: " + messageID);

                    System.out.print("Send, Store, or Disregard? ");
                    String action = scanner.nextLine();
                    System.out.println(messageService.SentMessage(action, messageID, recipient,
                            hash, messageText));
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                messageService.loadStoredMessages();

                int storedChoice = -1;
                while (storedChoice != 0) {
                    System.out.println();
                    System.out.println("--- Stored Messages ---");
                    System.out.println("a) Display sender and recipient of all stored messages");
                    System.out.println("b) Display the longest stored message");
                    System.out.println("c) Search for a message by Message ID");
                    System.out.println("d) Search for messages by recipient");
                    System.out.println("e) Delete a message using its message hash");
                    System.out.println("f) Display report of all sent messages");
                    System.out.println("0) Back to main menu");
                    System.out.print("Choose an option: ");
                    String sub = scanner.nextLine();

                    if (sub.equalsIgnoreCase("a")) {
                        System.out.println(messageService.displayStoredMessageRecipients());
                    } else if (sub.equalsIgnoreCase("b")) {
                        System.out.println(messageService.displayLongestStoredMessage());
                    } else if (sub.equalsIgnoreCase("c")) {
                        System.out.print("Enter Message ID: ");
                        String id = scanner.nextLine();
                        System.out.println(messageService.searchByMessageID(id));
                    } else if (sub.equalsIgnoreCase("d")) {
                        System.out.print("Enter recipient number: ");
                        String recipient = scanner.nextLine();
                        System.out.println(messageService.searchByRecipient(recipient));
                    } else if (sub.equalsIgnoreCase("e")) {
                        System.out.print("Enter message hash: ");
                        String hash = scanner.nextLine();
                        System.out.println(messageService.deleteMessageByHash(hash));
                    } else if (sub.equalsIgnoreCase("f")) {
                        System.out.println(messageService.displayReport());
                    } else if (sub.equals("0")) {
                        storedChoice = 0;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
            }
        }

        System.out.println("Total messages sent: " + messageService.returnTotalMessagesSent());
        scanner.close();
    }
}