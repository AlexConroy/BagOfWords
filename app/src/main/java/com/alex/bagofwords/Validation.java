package com.alex.bagofwords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    static boolean validPassword(String password) {
        String passwordPattern = "[!-~]{6,30}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    static boolean matchingPassword(String password, String confirmPassword) {
        return (password.equals(confirmPassword));
    }

    static boolean differentPasswords(String currentPassword, String newPassword) {
        return !currentPassword.equals(newPassword);
    }

    static boolean validEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; // Email regular expression.
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    static boolean matchingEmail(String currentEmail, String newEmail) {
        return (currentEmail.equals(newEmail));
    }

    static boolean fieldNotEmpty(String field) {
        return !field.matches("");
    }

    static boolean validUsername(String username) {
        String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_\\?\\$@]{1,14}$";
        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    static boolean validName(String name) {
        return (name.length() > 2 && name.length() <=30);
    }



}
