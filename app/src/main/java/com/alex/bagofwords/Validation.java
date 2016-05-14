package com.alex.bagofwords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    static boolean validPassword(String password) {
        return password.length() >= 6;
    }

    static boolean matchingPassword(String password, String confirmPassword) {
        return (password.equals(confirmPassword));
    }

    static boolean validEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; // Email regular expression.
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    static boolean fieldNotEmpty(String field) {
        return !field.matches("");
    }

    static boolean validUsername(String username) {

        return !username.matches("");
    }


    static boolean validName(String name) {
        return (name.length() >= 2 && name.length() <=30);
    }



}
