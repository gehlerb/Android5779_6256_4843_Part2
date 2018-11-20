package com.example.baruch.android5779_6256_4843_part2.model.entities;

public final class Exceptions {
    public static boolean checkOnlyLetters(String value) {
        return value.matches("[a-zA-Z א-ת]*");
    }

    public static boolean checkOnlyNumbers(String value) {
        return value.matches("[0-9]*");
    }

    public static boolean checkEmail(String email) {
        return email.equals("") || email.matches("^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");
    }
}
