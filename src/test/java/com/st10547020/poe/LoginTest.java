package com.st10547020.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginTest {

    private final Login login = new Login();

    @Test
    public void testUserNameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUserNameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }
}