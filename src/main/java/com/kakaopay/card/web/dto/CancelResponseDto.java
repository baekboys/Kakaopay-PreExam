package com.kakaopay.card.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelResponseDto extends ResponseDto  {
    // 관리번호
    private String managementId = "";
    // 거래시간
    private String transactionTime = "";

    @Builder
    public CancelResponseDto(String errorCode, String errorMessage, String managementId, String transactionTime) {
        super(errorCode,errorMessage);
        this.managementId = managementId;
        this.transactionTime = transactionTime;
    }
}

