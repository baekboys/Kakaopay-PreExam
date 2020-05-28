package com.kakaopay.card.Exception;

import lombok.Getter;

@Getter
public class PaymentBizException extends BizException {

    public PaymentBizException(BizExceptionType bizExceptionType) {
        super(bizExceptionType);
    }
}
