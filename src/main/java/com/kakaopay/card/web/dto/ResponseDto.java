package com.kakaopay.card.web.dto;

import lombok.Data;

@Data
public class ResponseDto {
    // 에러코드
    protected String errorCode = "";
    // 에러메시지
    protected String errorMessage = "";

    public ResponseDto(String errorCode, String errorMessage) {
        this.errorCode = errorCode == null ? "" : errorCode;
        this.errorMessage = errorMessage == null ? "" : errorMessage;
    }
}
