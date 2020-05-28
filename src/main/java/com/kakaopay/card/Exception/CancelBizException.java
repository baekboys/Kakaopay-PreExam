package com.kakaopay.card.Exception;

import lombok.Getter;

@Getter
public class CancelBizException extends BizException {

    public CancelBizException(BizExceptionType bizExceptionType) {
        super(bizExceptionType);
    }
}
