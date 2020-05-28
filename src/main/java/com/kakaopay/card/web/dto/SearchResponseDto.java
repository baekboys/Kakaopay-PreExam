package com.kakaopay.card.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchResponseDto extends ResponseDto {
    // 관리번호
    private String managementId = "";

    // 카드번호
    private String cardNum = "";
    // 유효기간
    private String expired = "";
    // CVC
    private String cvc = "";

    // 결제/취소 구분
    private String paymentType = "";
    // 금액
    private String amount = "";
    // 부가가치세
    private String vat = "";

    // 거래시간
    private String transactionTime = "";

    @Builder
    public SearchResponseDto(String errorCode, String errorMessage, String managementId, String cardNum, String expired, String cvc, String paymentType, String amount, String vat, String transactionTime) {
        super(errorCode,errorMessage);
        this.managementId = managementId;
        this.cardNum = cardNum;
        this.expired = expired;
        this.cvc = cvc;
        this.paymentType = paymentType;
        this.amount = amount;
        this.vat = vat;
        this.transactionTime = transactionTime;
    }
}

