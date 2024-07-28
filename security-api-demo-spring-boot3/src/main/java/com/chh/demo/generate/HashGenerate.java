package com.chh.demo.generate;

import com.chh.util.HashUtils;

public class HashGenerate {

    public static void main(String[] args) {
        String testString = "Hello, Hashing!";
        String encodedHash = HashUtils.computeHash(testString, "SHA-256");
        System.out.println("SHA-256 hash of '" + testString + "': " + encodedHash);
    }
}
