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
    private String sender;
    private static String currentSender = "Unknown";
    private static int totalMessagesSent = 0;

    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();
    private static List<String> sentMessagesLog = new ArrayList<>();

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Sets the username of the currently logged-in user, used as the
     * sender for all messages created afterwards.
     *
     * @param username the logged-in user's username
     */
    public static void setCurrentSender(String username) {
        currentSender = username;
    }

    /**
     * Clears all static arrays and counters. Intended for use between
     * unit tests so that each test starts from a clean state.
     */
    public static void resetArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        sentMessagesLog.clear();
        totalMessagesSent = 0;
    }

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
        Message m = new Message();
        m.messageID = messageID;
        m.recipient = recipient;
        m.messageHash = messageHash;
        m.messageText = messageText;
        m.sender = currentSender;

        messageIDs.add(messageID);
        messageHashes.add(messageHash);

        if (choice.equalsIgnoreCase("send")) {
            totalMessagesSent++;
            sentMessages.add(m);
            sentMessagesLog.add("Message ID: " + messageID + ", Message Hash: " + messageHash
                    + ", Recipient: " + recipient + ", Message: " + messageText);
            return "Message successfully sent.";
        } else if (choice.equalsIgnoreCase("store")) {
            storedMessages.add(m);
            storeMessage(messageID, recipient, messageHash, messageText);
            return "Message successfully stored.";
        } else if (choice.equalsIgnoreCase("disregard")) {
            disregardedMessages.add(m);
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
            messageObject.put("sender", currentSender);

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

    /**
     * Reads all messages from the JSON file and populates the storedMessages array.
     * Clears the array first so repeated calls do not duplicate entries.
     * JSON handling adapted from: stleary. (n.d.) JSON in Java, GitHub repository.
     * Available at: https://github.com/stleary/JSON-java (Accessed: 17 September 2026).
     */
    public void loadStoredMessages() {
        storedMessages.clear();
        try {
            File file = new File("storedMessages.json");
            if (!file.exists()) {
                return;
            }
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isEmpty()) {
                return;
            }
            JSONArray messagesArray = new JSONArray(content);
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONObject obj = messagesArray.getJSONObject(i);
                Message m = new Message();
                m.messageID = obj.getString("messageID");
                m.recipient = obj.getString("recipient");
                m.messageHash = obj.getString("messageHash");
                m.messageText = obj.getString("message");
                m.sender = obj.has("sender") ? obj.getString("sender") : "Unknown";
                storedMessages.add(m);
            }
        } catch (IOException e) {
            System.out.println("Error loading stored messages: " + e.getMessage());
        }
    }

    /**
     * Displays the sender and recipient of every stored message.
     *
     * @return a formatted string listing the sender and recipient of each stored message
     */
    public String displayStoredMessageRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(m.sender)
              .append(", Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Finds and displays the longest stored message.
     *
     * @return the text of the longest stored message
     */
    public String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    /**
     * Searches for a message by its message ID among stored and sent messages,
     * and returns the recipient and message text.
     *
     * @param id the message ID to search for
     * @return the recipient and message text, or a not-found message
     */
    public String searchByMessageID(String id) {
        for (Message m : storedMessages) {
            if (m.messageID.equals(id)) {
                return formatRecipientAndMessage(m);
            }
        }
        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) {
                return formatRecipientAndMessage(m);
            }
        }
        return "No message found with that ID.";
    }

private String formatRecipientAndMessage(Message m) {
    if (m.recipient == null || m.recipient.isEmpty()) {
        return m.messageText;
    }
    return m.recipient + " " + m.messageText;
}
    /**
     * Searches for all messages, sent or stored, for a particular recipient.
     *
     * @param recipient the recipient's cell phone number
     * @return a formatted string of all matching messages
     */
    public String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) {
                sb.append(m.messageText).append(" ");
            }
        }
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) {
                sb.append(m.messageText).append(" ");
            }
        }
        if (sb.length() == 0) {
            return "No messages found for that recipient.";
        }
        return sb.toString().trim();
    }

    /**
     * Deletes a stored message using its message hash.
     *
     * @param hash the message hash to search for and delete
     * @return a confirmation message including the deleted message's text,
     *         or a not-found message
     */
    public String deleteMessageByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (m.messageHash.equals(hash)) {
                storedMessages.remove(i);
                return "Message: \"" + m.messageText + "\" has been successfully deleted.";
            }
        }
        return "No message found with that hash.";
    }

    /**
     * Displays a report listing the message hash, recipient, and message
     * text of every sent message.
     *
     * @return a formatted report of all sent messages
     */
    public String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.messageHash)
              .append(", Recipient: ").append(m.recipient)
              .append(", Message: ").append(m.messageText)
              .append("\n");
        }
        return sb.toString().trim();
    }
}