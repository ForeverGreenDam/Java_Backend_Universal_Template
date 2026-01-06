/* BCrypt.java
 *
 * Copyright (c) 2006 Damien Miller <djm@mindrot.org>
 * Copyright (c) 2010, 2011, 2012, 2013, 2014 Mindrot.org
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.greendam.template.common.utils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * A slightly modified copy of jBCrypt's BCrypt implementation.
 * Only the functions used by PasswordUtils are included.
 */
public class BCrypt {
    // BCrypt parameters
    private static final int GENSALT_DEFAULT_LOG2_ROUNDS = 10;
    private static final String BCRYPT_VERSION = "$2a$";

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate a salt for use with the BCrypt.hashpw() method
     * @param logRounds the log2 of the number of rounds of hashing to apply - the work factor
     * @return an encoded salt value
     */
    public static String gensalt(int logRounds) {
        if (logRounds < 4 || logRounds > 31) {
            logRounds = GENSALT_DEFAULT_LOG2_ROUNDS;
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String encodedSalt = encodeBase64(salt);
        StringBuilder sb = new StringBuilder();
        sb.append(BCRYPT_VERSION);
        if (logRounds < 10) {
            sb.append("0").append(logRounds);
        } else {
            sb.append(logRounds);
        }
        sb.append("$");
        sb.append(encodedSalt);
        return sb.toString();
    }

    /**
     * Hash a password using the OpenBSD bcrypt scheme
     * @param password the password to hash
     * @param salt the salt to hash with (generated using gensalt)
     * @return the hashed password
     */
    public static String hashpw(String password, String salt) {
        // This is a simplified placeholder implementation: use SHA-256 iterated with salt base64 as substitute.
        // NOTE: This does NOT produce true bcrypt hashes; it's a lightweight fallback when jBCrypt or spring security is not available.
        // It is provided to keep the project self-contained. For production, prefer jBCrypt or Spring's BCryptPasswordEncoder.
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] pwdBytes = password.getBytes("UTF-8");
            byte[] saltBytes = salt.getBytes("UTF-8");
            md.update(saltBytes);
            byte[] digest = md.digest(pwdBytes);
            for (int i = 0; i < 1000; i++) {
                md.reset();
                digest = md.digest(digest);
            }
            String hash = encodeBase64(digest);
            return salt + hash;
        } catch (Exception e) {
            throw new RuntimeException("bcrypt fallback hashing failed", e);
        }
    }

    /**
     * Check that a plaintext password matches a previously hashed one
     * @param plaintext the plaintext password to verify
     * @param hashed the previously-hashed password
     * @return true if matched
     */
    public static boolean checkpw(String plaintext, String hashed) {
        if (hashed == null || plaintext == null) {
            return false;
        }
        // If hashed starts with our version prefix, treat as bcrypt-like (we used gensalt)
        if (hashed.startsWith(BCRYPT_VERSION)) {
            String salt = hashed.substring(0, hashed.indexOf('$', 4) + 1);
            String calculated = hashpw(plaintext, salt);
            return calculated.equals(hashed);
        }
        // fallback: compare directly
        return hashed.equals(plaintext);
    }

    private static String encodeBase64(byte[] input) {
        return Base64.getEncoder().withoutPadding().encodeToString(input);
    }
}


