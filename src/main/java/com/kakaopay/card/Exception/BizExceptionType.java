package com.kakaopay.card.Exception;

import lombok.Getter;

@Getter
public enum BizExceptionType {

    BASIC_ERROR("000", "에러가 발생하였습니다."),

    HTTP_METHOD_ERROR("001", "허용하지 않는 메소드입니다."),
    MEDIA_TYPE_ERROR("002", "허용하지 않는 MEDIA_TYPE입니다. JSON만 지원합니다."),
    MSG_NOT_READ_ERROR("003", "부정확한 JSON 형식 입니다."),

    INVALID_PARAM("100", "부정확한 파라미터입니다."),
    INVALID_CARD_NUM("101", "부정확한 카드번호입니다."),
    INVALID_EXPIERD_NUM("102", "부정확한 유효기간입니다."),
    INVALID_CVC_NUM("103", "부정확한 CVC번호입니다."),
    INVALID_INSTALL_NUM("104", "부정확한 할부기간입니다."),
    INVALID_AMOUNT_NUM("105", "부정확한 금액입니다."),
    INVALID_VAT_NUM("106", "부정확한 부가가치세입니다."),
    INVALID_VAT_EXCEED("107", "부가가치세가 금액보다 클 수 없습니다."),
    INVALID_NUM("108", "숫자만 입력 가능합니다."),
    INVALID_MANAGEMENT_ID("109", "부정확한 관리번호입니다."),
    INVALID_AMOUNT_EXCEED("110", "가능금액을 초과하였습니다."),

    DATA_NOT_FONUD("200", "데이터를 찾을 수 없습니다."),
    DATA_PAYMENT_NOT_FONUD("201", "결제데이터를 찾을 수 없습니다."),
    DATA_CANCEL_NOT_FONUD("202", "취소데이터를 찾을 수 없습니다."),
    DATA_NOT_SAVE("203", "데이터 저장에 실패하였습니다."),
    
    ENCRYPTOR_FAIL("300", "암복호화에 실패하였습니다."),
    ENCRYPTOR_ENC_FAIL("301", "데이터 암호화에 실패하였습니다."),
    ENCRYPTOR_DEC_FAIL("302", "데이터 복호화에 실패하였습니다."),

    CANCEL_NOT_PROCESS("400", "취소를 진행할 수 없습니다."),
    CANCEL_NOT_AMOUCNT("401", "취소불가한 금액입니다."),
    CANCEL_NOT_VAT("402", "취소불가한 부가가치세입니다."),
    CANCEL_FINISHED("403", "이미 취소된 건입니다."),

    PAYMENT_NOT_PROCESS("500", "결제를 진행 할 수 없습니다.")
    ;

    String code;
    String message;

    BizExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
