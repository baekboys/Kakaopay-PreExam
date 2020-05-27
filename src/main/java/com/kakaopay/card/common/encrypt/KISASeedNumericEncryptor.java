package com.kakaopay.card.common.encrypt;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.common.KISA_SEED_CBC;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KISASeedNumericEncryptor {

    private final static String CHARSET = "utf-8";
    private final static String PBUserKey = "kakaopaypreexam!";
    private final static String DEFAULT_IV = "1234567890987654";

    private final static byte pbUserKey[] = PBUserKey.getBytes();
    private final static byte bszIV[] = DEFAULT_IV.getBytes();

    public static String encrypt(String plainStr) throws BizException {

        // 숫자만 구성된 경우 암호화 된 데이터가 아니므로 파라미터 그대로 리턴
        if(!StringUtils.isNumeric(plainStr)) {
            return plainStr;
        }

        byte[] encBytes = null;

        try {
            encBytes = KISA_SEED_CBC.SEED_CBC_Encrypt(pbUserKey, bszIV, plainStr.getBytes(CHARSET), 0, plainStr.getBytes(CHARSET).length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] encArray = Base64.getEncoder().encode(encBytes);

        if(encArray == null) {
            throw new BizException(BizExceptionType.ENCRYPTOR_ENC_FAIL);
        }

        String encStr = new String(encArray, StandardCharsets.UTF_8);

        return encStr;
    }

    public static String decrypt(String encStr) throws BizException {

        // 숫자만 구성된 경우 암호화 된 데이터가 아니므로 파라미터 그대로 리턴
        if(StringUtils.isNumeric(encStr)) {
            return encStr;
        }

        byte[] enc = Base64.getDecoder().decode(encStr);

        byte[] decBytes = null;
        String plainStr = "";

        decBytes = KISA_SEED_CBC.SEED_CBC_Decrypt(pbUserKey, bszIV, enc, 0, enc.length);

        if(decBytes == null) {
            throw new BizException(BizExceptionType.ENCRYPTOR_DEC_FAIL);
        }

        plainStr = new String(decBytes, StandardCharsets.UTF_8);

        return plainStr;
    }

}
