package com.kakaopay.card.web.dto;

import lombok.*;

@Data
public class ResponseDto {
    // 에러코드
    protected String errorCode = "";
    // 에러메시지
    protected String errorMessage = "";
}
