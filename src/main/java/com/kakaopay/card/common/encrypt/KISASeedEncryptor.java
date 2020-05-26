package com.kakaopay.card.common.encrypt;

import com.kakaopay.card.common.KISA_SEED_CBC;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KISASeedEncryptor {

    private final static String CHARSET = "utf-8";
    private final static String PBUserKey = "kakaopaypreexam!";
    private final static String DEFAULT_IV = "1234567890987654";

    private final static byte pbUserKey[] = PBUserKey.getBytes();
    private final static byte bszIV[] = DEFAULT_IV.getBytes();

    public static String encrypt(String plainStr) {

        byte[] encBytes = null;

        try {
            encBytes = KISA_SEED_CBC.SEED_CBC_Encrypt(pbUserKey, bszIV, plainStr.getBytes(CHARSET), 0, plainStr.getBytes(CHARSET).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] encArray = Base64.getEncoder().encode(encBytes);
        String encStr = new String(encArray, StandardCharsets.UTF_8);

        return encStr;
    }

    public static String decrypt(String encStr) {
        byte[] enc = Base64.getDecoder().decode(encStr);

        byte[] decBytes = null;
        String plainStr = "";

        decBytes = KISA_SEED_CBC.SEED_CBC_Decrypt(pbUserKey, bszIV, enc, 0, enc.length);
        plainStr = new String(decBytes, StandardCharsets.UTF_8);

        return plainStr;
    }

}
