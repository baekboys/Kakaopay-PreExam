package com.kakaopay.card.Exception;

import lombok.Getter;

@Getter
public enum BizExceptionType {
    INVALID_PARAM("100", "부정확한 파라미터입니다."),
    INVALID_CARD_NUM("101", "부정확한 카드번호입니다."),
    INVALID_EXPIERD_NUM("102", "부정확한 유효기간입니다."),
    INVALID_CVC_NUM("103", "부정확한 CVC번호입니다."),
    INVALID_INSTALL_NUM("104", "부정확한 할부기간입니다."),
    INVALID_AMOUNT_NUM("105", "부정확한 금액입니다."),
    INVALID_VAT_NUM("106", "부정확한 금액입니다."),
    INVALID_NUM("107", "숫자만 입력 가능합니다."),

    DATA_NOT_FONUD("200", "데이터를 찾을 수 없습니다."),
    DATA_PAYMENT_NOT_FONUD("201", "결제데이터를 찾을 수 없습니다."),
    DATA_CANCEL_NOT_FONUD("202", "취소데이터를 찾을 수 없습니다."),
    
    ENCRYPTOR_FAIL("300", "암복호화에 실패하였습니다."),
    ENCRYPTOR_ENC_FAIL("301", "데이터 암호화에 실패하였습니다."),
    ENCRYPTOR_DEC_FAIL("302", "데이터 복호화에 실패하였습니다."),
    ;

    String code;
    String message;

    BizExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
