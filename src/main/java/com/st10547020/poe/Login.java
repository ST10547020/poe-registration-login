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
    /**
     * Checks that the password meets the complexity rules:
     * at least eight characters, containing at least one
     * capital letter, one number, and one special character.
     *
     * @param password the password to validate
     * @return true if the password meets all complexity rules, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasDigit && hasSpecialChar;
    }
}