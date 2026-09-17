package com.st10547020.poe;

import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Represents a single chat message, including its ID, recipient,
 * content, hash, and sent/stored/discarded status.
 */
public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessagesSent = 0;

    /**
     * Generates a random ten-digit message ID.
     *
     * @return the generated message ID
     */
    public String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        this.messageID = id.toString();
        return this.messageID;
    }

    /**
     * Checks that the message ID is no more than ten characters.
     *
     * @param id the message ID to validate
     * @return true if valid, false otherwise
     */
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    /**
     * Checks that the recipient cell number is no more than ten
     * characters long and starts with an international country code.
     *
     * @param recipientCell the recipient's cell phone number
     * @return a message indicating success or the specific validation error
     */
    public String checkRecipientCell(String recipientCell) {
        if (recipientCell != null && recipientCell.matches("^\\+\\d{9,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    /**
     * Checks that the message does not exceed 250 characters.
     *
     * @param message the message text to validate
     * @return a message indicating success or how many characters over the limit
     */
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = message.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Creates the message hash using the first two digits of the message ID,
     * the message number, and the first and last words of the message,
     * all displayed in capital letters.
     *
     * @param messageID the message's ID
     * @param messageNumber the sequential number of the message
     * @param message the message text
     * @return the generated message hash
     */
    public String createMessageHash(String messageID, int messageNumber, String message) {
        String idPrefix = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    private static List<String> sentMessagesLog = new ArrayList<>();

    /**
     * Allows the user to choose to send, store for later, or discard
     * the message.
     *
     * @param choice the user's choice: "send", "store", or "disregard"
     * @param messageID the message's ID
     * @param recipient the recipient's cell phone number
     * @param messageHash the generated message hash
     * @param messageText the message text
     * @return a message indicating the outcome of the chosen action
     */
    public String SentMessage(String choice, String messageID, String recipient,
                               String messageHash, String messageText) {
        if (choice.equalsIgnoreCase("send")) {
            totalMessagesSent++;
            sentMessagesLog.add("Message ID: " + messageID + ", Message Hash: " + messageHash
                    + ", Recipient: " + recipient + ", Message: " + messageText);
            return "Message successfully sent.";
        } else if (choice.equalsIgnoreCase("store")) {
            storeMessage(messageID, recipient, messageHash, messageText);
            return "Message successfully stored.";
        } else if (choice.equalsIgnoreCase("disregard")) {
            return "Press 0 to delete the message.";
        }
        return "Invalid choice.";
    }

    /**
     * Returns the full details of all messages sent while the program is running.
     *
     * @return the list of sent messages as a formatted string
     */
    public String printMessages() {
        return String.join("\n", sentMessagesLog);
    }

    /**
     * Returns the total number of messages sent.
     *
     * @return the total number of messages sent
     */
    public int returnTotalMessagesSent() {
        return totalMessagesSent;
    }

    /**
     * Stores a message in a JSON file, appending to any existing messages.
     * JSON handling adapted from: stleary. (n.d.) JSON in Java, GitHub repository.
     * Available at: https://github.com/stleary/JSON-java (Accessed: 17 September 2026).
     *
     * @param messageID the message's ID
     * @param recipient the recipient's cell phone number
     * @param messageHash the generated message hash
     * @param messageText the message text
     */
    public void storeMessage(String messageID, String recipient, String messageHash,
                              String messageText) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("messageID", messageID);
            messageObject.put("recipient", recipient);
            messageObject.put("messageHash", messageHash);
            messageObject.put("message", messageText);

            File file = new File("storedMessages.json");
            JSONArray messagesArray;
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                messagesArray = content.isEmpty() ? new JSONArray() : new JSONArray(content);
            } else {
                messagesArray = new JSONArray();
            }
            messagesArray.put(messageObject);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(messagesArray.toString(4));
            }
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}