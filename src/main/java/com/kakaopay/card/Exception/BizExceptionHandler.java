package com.kakaopay.card.Exception;

import com.kakaopay.card.web.dto.CancelResponseDto;
import com.kakaopay.card.web.dto.PaymentResponseDto;
import com.kakaopay.card.web.dto.ResponseDto;
import com.kakaopay.card.web.dto.SearchResponseDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BizExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException he) {
        he.printStackTrace();
        ResponseDto responseDto = new ResponseDto(BizExceptionType.HTTP_METHOD_ERROR.getCode(), he.getMethod() + "는 " +BizExceptionType.HTTP_METHOD_ERROR.getMessage());
        return responseDto;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseDto httpMessageNotReadableException(HttpMessageNotReadableException re) {
        re.printStackTrace();
        ResponseDto responseDto = new ResponseDto(BizExceptionType.MSG_NOT_READ_ERROR.getCode(), BizExceptionType.MSG_NOT_READ_ERROR.getMessage());
        return responseDto;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseDto httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException me) {
        me.printStackTrace();
        ResponseDto responseDto = new ResponseDto(BizExceptionType.MEDIA_TYPE_ERROR.getCode(),  me.getContentType()+ "는 " + BizExceptionType.MEDIA_TYPE_ERROR.getMessage());
        return responseDto;
    }



    @ExceptionHandler(Exception.class)
    public ResponseDto handleException(Exception e) {
        e.printStackTrace();
        ResponseDto responseDto = new ResponseDto(BizExceptionType.BASIC_ERROR.getCode(), BizExceptionType.BASIC_ERROR.getMessage());
        return responseDto;
    }

    @ExceptionHandler(BizException.class)
    public ResponseDto handleBizException(BizException be){
        be.printStackTrace();
        ResponseDto responseDto = new ResponseDto(be.getBizExceptionType().getCode(), be.getBizExceptionType().getMessage());

        return responseDto;
    }

    // 결제 관련 예외처리
    @ExceptionHandler(PaymentBizException.class)
    public PaymentResponseDto handlePaymentBizException(PaymentBizException pbe){
        pbe.printStackTrace();
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
        sbe.printStackTrace();
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
        cbe.printStackTrace();
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
