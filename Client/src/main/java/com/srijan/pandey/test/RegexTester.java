package com.srijan.pandey.test;

import java.util.regex.Pattern;

public class RegexTester {

    public static void main(String[] args) {
        boolean matchesPattern = Pattern.matches("\\w+" , "helloworld");

        System.out.println(matchesPattern);
    }
}
