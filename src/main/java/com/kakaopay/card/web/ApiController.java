package com.kakaopay.card.web;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.common.ManagementIdGenerator;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final PaymentService paymentService;

    @GetMapping("/")
    public String index(Model model) {
        return "hello";
    }

    @PostMapping(value="/api/v1/test")
    public String test(@RequestBody PaymentRequestDto requestDto) {
        return "test";
    }

    @PostMapping(value="/api/v1/payment")
    public PaymentResponseDto payment(@RequestBody PaymentRequestDto requestDto) throws BizException {

        if(!requestDto.isValidate()) {
            throw new BizException(BizExceptionType.INVALID_PARAM);
        }

        PaymentResponseDto paymentResponseDto  = paymentService.save(requestDto);

        return paymentResponseDto;
    }

    /*@GetMapping(value="/api/v1/search")
    public PaymentResponseDto searchFromManagementId(@RequestBody SearchReqeustDto requestDto) {

        if(!requestDto.isValidate()) {
            PaymentResponseDto errorPaymentResponseDto = new PaymentResponseDto();
            errorPaymentResponseDto.setErrorCode("100");
            errorPaymentResponseDto.setErrorMessage("부적합한 파라미터");
            return errorPaymentResponseDto;
        }

        Payment payment  = paymentService.save(requestDto);

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .payment(payment)
                .build();

        return paymentResponseDto;
    }*/
}