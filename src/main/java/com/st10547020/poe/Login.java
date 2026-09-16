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

    /**
     * Checks that the cell phone number contains the international
     * country code and is no more than ten characters long.
     * Regex pattern adapted from: Baeldung. (n.d.) Java Regex Validate Phone Numbers.
     * Available at: https://www.baeldung.com/java-regex-validate-phone-numbers
     * (Accessed: 16 September 2026).
     *
     * @param cellPhoneNumber the cell phone number to validate
     * @return true if correctly formatted, false otherwise
     */
        public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        return cellPhoneNumber.matches("^\\+\\d{9,12}$");
    }

    private String storedUsername;
    private String storedPassword;
    private String storedCellPhoneNumber;
    private String storedFirstName;
    private String storedLastName;

    /**
     * Registers a new user by validating the username, password, and cell
     * phone number, and returns the appropriate registration message.
     *
     * @param username the chosen username
     * @param password the chosen password
     * @param cellPhoneNumber the cell phone number
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return a message indicating success or the specific validation error
     */
    public String registerUser(String username, String password, String cellPhoneNumber,
                                String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your "
                    + "username contains an underscore and is no more than five "
                    + "characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the "
                    + "password contains at least eight characters, a capital letter, "
                    + "a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number is incorrectly formatted or does not contain an "
                    + "international code; please correct the number and try again.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellPhoneNumber = cellPhoneNumber;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;

        return "Username successfully captured. Password successfully captured. "
                + "Cell phone number successfully added.";
    }

    private boolean lastLoginSuccessful;

    /**
     * Verifies that the entered username and password match the
     * details stored when the user registered.
     *
     * @param username the username entered at login
     * @param password the password entered at login
     * @return true if the credentials match, false otherwise
     */
    public boolean loginUser(String username, String password) {
        lastLoginSuccessful = username != null && username.equals(storedUsername)
                && password != null && password.equals(storedPassword);
        return lastLoginSuccessful;
    }

    /**
     * Returns the appropriate message for the outcome of the last login attempt.
     *
     * @return a welcome message on success, or a failure message otherwise
     */
    public String returnLoginStatus() {
        if (lastLoginSuccessful) {
            return "Welcome " + storedFirstName + ", " + storedLastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }        
}
