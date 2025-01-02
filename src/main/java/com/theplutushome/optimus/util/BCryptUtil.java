package com.theplutushome.optimus.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param plainPassword The plain text password to hash.
     * @return The hashed password.
     */
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Verifies a plain text password against a hashed password.
     *
     * @param plainPassword The plain text password.
     * @param hashedPassword The hashed password stored in the database.
     * @return True if the password matches, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}

