package com.kakaopay.card.Exception;

import lombok.Getter;

@Getter
public class SearchBizException extends BizException {

    public SearchBizException(BizExceptionType bizExceptionType) {
        super(bizExceptionType);
    }
}
