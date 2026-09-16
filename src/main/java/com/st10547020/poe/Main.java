package com.st10547020.poe;

import java.util.Scanner;

/**
 * Entry point of the application. Provides a console menu
 * allowing the user to register an account or log in.
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

        String registrationResult = login.registerUser(username, password, cellPhoneNumber,
                firstName, lastName);
        System.out.println(registrationResult);

        System.out.println();
        System.out.println("Please log in.");

        System.out.print("Username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus());

        scanner.close();
    }
}