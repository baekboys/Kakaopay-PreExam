package com.kakaopay.card.common;

import com.kakaopay.card.domain.PaymentType;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**********************************************************************
 * 62진법 인코딩을 하여 숫자형 문자열을 길이가 짧아지는 문자열로 변환
 * 1) 문자열 99999999999999999999999999999999999 : 35자리
 *   변환시 : 8NH3IwfZL7k4Z6stxUMT 20자리
 * 2) 문자열 9999999999999999999999999999999999 : 34자리
 *   변환시 : SziYfCNzI8cTI2Qyohh 19자리
 *********************************************************************/
public class ManagementIdGenerator {

    private static final String fillerChar = "-";
    private static final int fillerSize = 20;

    public static String getManagementIdByPaymentTypeAndCardNum(PaymentType paymentType, String cardnum) {
        String managementId = "";

        // 숫자로 구성된 문자열을 조합하기 위해 SB생성
        StringBuilder sb = new StringBuilder();


        // (1) 결제유형(1 : 결제 PAYMENT, 2 : 취소  CANCEL)
        String paymentTypeValue = paymentType.getTypeNum();
        sb.append(paymentTypeValue);

        // (2) 카드번호길이의 뒷자리 (10~16자리 이므로 0~6만 나올 수 있음)
        String cardLengthLastNum = Integer.toString(cardnum.length() % 10);
        sb.append(cardLengthLastNum);

        // (3) 카드번호 10~16자리, 16자리 미만은 0으로 right padding
        sb.append( StringUtils.rightPad(cardnum, 16, "0"));

        // (3) 시간(yyMMddhhmmssSS) 14자리
        String currentTime = DateTimeFormatter.ofPattern("yyMMddHHmmssSS").format(LocalDateTime.now());
        sb.append(currentTime);

        // (4) 3자리 랜덤 숫자
        String random3num = Integer.toString(new Random().nextInt(1000));
        sb.append(StringUtils.leftPad(random3num, 3, "0" ));

        // (5) 35자리의 숫자로 구성 된 문자열을 인코딩 => 최대 20자리의 문자열로 치환됨
        String base62str = getBase62From10(sb.toString());

        // (6) 20자리가 안되면 padding할 문자열을 추가하여 20자리로 만들어줌
        managementId = StringUtils.rightPad(base62str, fillerSize, fillerChar);

        return managementId;
    }

    private static final BigInteger RADIX = BigInteger.valueOf(62);
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
    };

    /**
     *
     * @param number a positive number in base 10
     *
     * @return the same number, in base 62
     */
    public static String getBase62From10(String number) {
        char[] buf = new char[number.length()];
        int charPos = number.length() - 1;

        BigInteger i = new BigInteger(number);
        BigInteger radix = BigInteger.valueOf(62);

        while (i.compareTo(radix) >= 0) {
            buf[charPos--] = DIGITS[i.mod(radix).intValue()];
            i = i.divide(radix);
        }
        buf[charPos] = DIGITS[i.intValue()];

        return new String(buf, charPos, (number.length() - charPos));
    }

    /**
     *
     * @param number a positive number in base 62
     *
     * @return the same number, in base 10
     */
    public static String getBase10From62(String number) {
        BigInteger value = BigInteger.ZERO;
        for (char c : number.toCharArray()) {
            value = value.multiply(RADIX);
            if ('0' <= c && c <= '9') {
                value = value.add(BigInteger.valueOf(c - '0'));
            }
            if ('a' <= c && c <= 'z') {
                value = value.add(BigInteger.valueOf(c - 'a' + 10));
            }
            if ('A' <= c && c <= 'Z') {
                value = value.add(BigInteger.valueOf(c - 'A' + 36));
            }
        }
        return value.toString();
    }
}
