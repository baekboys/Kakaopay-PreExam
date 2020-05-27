package com.kakaopay.card.common;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;

public class MaskingUtil {

    private static final int presize = 6;
    private static final int postSize = 3;

    private static final char maskingChar = '*';

    public static String getMaskingCardNum(String cardNum) throws BizException {
        // 카드번호 전체길이
        int cardNumLength = cardNum.length();

        if( !(cardNumLength >= 10 && cardNumLength <= 16) ) {
            throw new BizException(BizExceptionType.INVALID_CARD_NUM);
        }


        // 카드번호 전체길이만큼 객체사이즈 생성
        StringBuilder sb = new StringBuilder(cardNumLength);

        // 마스킹이 안될 카드번호 앞 자리
        String preCardNum = cardNum.substring(0, 6);
        sb.append(preCardNum);

        // 마스킹
        for(int i = 0; i < cardNumLength - (presize + postSize) ; i++) {
            sb.append(maskingChar);
        }

        // 마스킹이 안될 카드번호 뒷 자리
        String postCardNum = cardNum.substring(cardNumLength - 3);
        sb.append(postCardNum);

        return sb.toString();
    }
}
