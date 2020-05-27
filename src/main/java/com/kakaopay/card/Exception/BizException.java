package com.kakaopay.card.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BizException extends RuntimeException {

    private BizExceptionType bizExceptionType;

    @Builder
    public BizException(BizExceptionType bizExceptionType) {
        super();
        this.bizExceptionType = bizExceptionType;
    }
}
