package com.st10547020.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    private final Message message = new Message();

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
}