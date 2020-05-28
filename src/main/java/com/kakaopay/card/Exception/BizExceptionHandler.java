package com.kakaopay.card.Exception;

import com.kakaopay.card.web.dto.CancelResponseDto;
import com.kakaopay.card.web.dto.PaymentResponseDto;
import com.kakaopay.card.web.dto.ResponseDto;
import com.kakaopay.card.web.dto.SearchResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BizExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseDto handleException(Exception e) {
        ResponseDto responseDto = new ResponseDto(BizExceptionType.BASIC_ERROR.getCode(), BizExceptionType.BASIC_ERROR.getMessage());

        return responseDto;
    }

    @ExceptionHandler(BizException.class)
    public ResponseDto handleBizException(BizException be){

        ResponseDto responseDto = new ResponseDto(be.getBizExceptionType().getCode(), be.getBizExceptionType().getMessage());

        return responseDto;
    }

    // 결제 관련 예외처리
    @ExceptionHandler(PaymentBizException.class)
    public PaymentResponseDto handlePaymentBizException(PaymentBizException pbe){

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .errorCode(pbe.getBizExceptionType().getCode())
                .errorMessage(pbe.getBizExceptionType().getMessage())
                .managementId("")
                .transactionTime("")
                .build()
                ;

        return paymentResponseDto;
    }

    // 조회 관련 예외처리
    @ExceptionHandler(SearchBizException.class)
    public SearchResponseDto handleSearchBizException(SearchBizException sbe){

        SearchResponseDto searchResponseDto = SearchResponseDto.builder()
                .errorCode(sbe.getBizExceptionType().getCode())
                .errorMessage(sbe.getBizExceptionType().getMessage())
                .managementId("")
                .cardNum("")
                .expired("")
                .cvc("")
                .paymentType("")
                .amount("")
                .vat("")
                .transactionTime("")
                .build()
                ;

        return searchResponseDto;
    }

    // 취소 관련 예외처리
    @ExceptionHandler(CancelBizException.class)
    public CancelResponseDto handlePaymentBizException(CancelBizException cbe){

        CancelResponseDto cancelResponseDto = CancelResponseDto.builder()
                .errorCode(cbe.getBizExceptionType().getCode())
                .errorMessage(cbe.getBizExceptionType().getMessage())
                .managementId("")
                .transactionTime("")
                .build()
                ;

        return cancelResponseDto;
    }
}
