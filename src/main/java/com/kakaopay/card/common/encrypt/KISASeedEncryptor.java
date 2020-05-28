package com.kakaopay.card.common.encrypt;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.common.KISA_SEED_CBC;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KISASeedEncryptor {

    private final static String CHARSET = "utf-8";
    private final static String PBUserKey = "kakaopaypreexam!";
    private final static String DEFAULT_IV = "1234567890987654";

    private final static byte pbUserKey[] = PBUserKey.getBytes();
    private final static byte bszIV[] = DEFAULT_IV.getBytes();

    // 카드정보 구분자 : ,로 구분
    private final static String cardInfoSeparator = ",";

    // 암호화
    public static String encrypt(String plainStr) throws BizException {

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

    // 카드번호, 유효기간, cvc값을 받아 구분자를 주고 암호화
    public static String encryptCardInfo(String cardNum, String expired, String cvc) {
        String plainInfo = cardNum + cardInfoSeparator + expired + cardInfoSeparator + cvc;
        String encCardInfo = encrypt(plainInfo);

        return encCardInfo;
    }

    // 복호화
    public static String decrypt(String encStr) throws BizException {

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
