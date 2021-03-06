package com.kakaopay.card.web.dto;

import lombok.*;

@Getter
public class PaymentResponseDto extends ResponseDto {
    // 관리번호
    private final String managementId;
    // 거래시간
    private final String transactionTime;

    @Builder
    public PaymentResponseDto(String errorCode, String errorMessage, String managementId, String transactionTime) {
        super(errorCode,errorMessage);
        this.managementId = managementId;
        this.transactionTime = transactionTime;
    }
}

