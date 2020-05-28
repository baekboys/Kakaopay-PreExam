package com.kakaopay.card.web.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
public class Response<T> {
    // 에러코드
    private String errorCode = "";
    // 에러메시지
    private String errorMsg = "";

    // 응답데이터
    private T data;

    @Builder
    public Response(String errorCode, String errorMsg, T data) {
        this.errorCode = errorCode == null ? "" : errorCode;
        this.errorMsg = errorMsg == null ? "" : errorMsg;
        this.data = data;
    }

    @Builder
    public Response(T data) {
        this.data = data;
    }
}
