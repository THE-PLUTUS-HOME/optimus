package com.theplutushome.optimus.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Function {
    public static String generateCryptomusSignature(Object data, String apiPayoutKey) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String dataEncoded = objectMapper.writeValueAsString(data);
        String dataBase64 = Base64.getEncoder().encodeToString(dataEncoded.getBytes(StandardCharsets.UTF_8));
        String input = dataBase64 + apiPayoutKey;

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public static String generateReferralCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int CODE_LENGTH = 8;

        SecureRandom random = new SecureRandom();
        StringBuilder referralCode = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            referralCode.append(CHARACTERS.charAt(index));
        }

        return referralCode.toString();
    }
}
