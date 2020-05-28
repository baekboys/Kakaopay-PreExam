package com.kakaopay.card.Exception;

import lombok.*;

@Getter
@NoArgsConstructor
public class BizException extends RuntimeException {
    // 예외 종류 열거자
    protected BizExceptionType bizExceptionType;

    @Builder
    public BizException(BizExceptionType bizExceptionType) {
        this.bizExceptionType = bizExceptionType;
    }
}
