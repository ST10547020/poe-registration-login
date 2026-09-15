package com.st10547020.poe;

/**
 * Handles user registration and login logic,
 * including validation of username, password, and cell phone number.
 */
public class Login {

    /**
     * Checks that the username contains an underscore
     * and is no more than five characters long.
     *
     * @param username the username to validate
     * @return true if the username is correctly formatted, false otherwise
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }
}