package com.gustavo.authenticationserver.otp;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OneTimePasswordUtils {
    public static String generateRandomOtp() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            int random = secureRandom.nextInt(9000) + 1000;

            return String.valueOf(random);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error when generating random code");
        }
    }
}
