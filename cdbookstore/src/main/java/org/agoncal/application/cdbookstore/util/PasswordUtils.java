package org.agoncal.application.cdbookstore.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtils {
    public static String digestPassword(final String plainTextPassword) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plainTextPassword.getBytes("UTF-8"));
            final byte[] passwordDigest = messageDigest.digest();
            return new String(Base64.getEncoder().encode(passwordDigest));
        } catch (final Exception exception) {
            throw new RuntimeException("Exception encoding password", exception);
        }
    }
}
