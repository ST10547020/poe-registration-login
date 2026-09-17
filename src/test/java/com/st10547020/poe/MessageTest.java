package com.st10547020.poe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    private final Message message = new Message();

    @BeforeEach
    public void resetBeforeEachTest() {
        Message.resetArrays();
    }

    @Test
    public void testMessageLengthWithinLimit() {
        String result = message.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testMessageLengthExceedsLimit() {
        String longMessage = "a".repeat(260);
        String result = message.checkMessageLength(longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        String result = message.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        String result = message.checkRecipientCell("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.", result);
    }

    @Test
    public void testMessageHashIsCorrect() {
        String hash = message.createMessageHash("0012345678", 0, "Hi tonight");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testMessageIDIsGenerated() {
        String id = message.generateMessageID();
        assertTrue(id.length() == 10);
    }

    /**
     * Populates the message arrays using the five official test data
     * messages provided in the assignment brief.
     */
    private void populateTestData() {
        String hash1 = message.createMessageHash("1111111111", 0, "Did you get the cake?");
        message.SentMessage("send", "1111111111", "+27834557896", hash1,
                "Did you get the cake?");

        String text2 = "Where are you? You are late! I have asked you to be on time.";
        String hash2 = message.createMessageHash("2222222222", 1, text2);
        message.SentMessage("store", "2222222222", "+27838884567", hash2, text2);

        String hash3 = message.createMessageHash("3333333333", 2, "Yohooo, I am at your gate.");
        message.SentMessage("disregard", "3333333333", "+27834484567", hash3,
                "Yohooo, I am at your gate.");

        String hash4 = message.createMessageHash("0838884567", 3, "It is dinner time!");
        message.SentMessage("send", "0838884567", "", hash4, "It is dinner time!");

        String text5 = "Ok, I am leaving without you.";
        String hash5 = message.createMessageHash("5555555555", 4, text5);
        message.SentMessage("store", "5555555555", "+27838884567", hash5, text5);
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        populateTestData();
        String log = message.printMessages();
        assertTrue(log.contains("Did you get the cake?"));
        assertTrue(log.contains("It is dinner time!"));
    }

    @Test
    public void testDisplayLongestStoredMessage() {
        populateTestData();
        String result = message.displayLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", result);
    }

    @Test
    public void testSearchForMessageID() {
        populateTestData();
        String result = message.searchByMessageID("0838884567");
        assertEquals("It is dinner time!", result);
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        populateTestData();
        String result = message.searchByRecipient("+27838884567");
        assertEquals("Where are you? You are late! I have asked you to be on time. "
                + "Ok, I am leaving without you.", result);
    }

    @Test
    public void testDeleteMessageUsingHash() {
        populateTestData();
        String text2 = "Where are you? You are late! I have asked you to be on time.";
        String hash2 = message.createMessageHash("2222222222", 1, text2);
        String result = message.deleteMessageByHash(hash2);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" "
                + "successfully deleted.", result);
    }

    @Test
    public void testDisplayReport() {
        populateTestData();
        String report = message.displayReport();
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }
}