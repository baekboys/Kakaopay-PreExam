package com.kakaopay.card.web.dto;

import com.kakaopay.card.domain.payment.Payment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentResponseDto {
    private String paymentId;
    private LocalDateTime paymentDate;

    public PaymentResponseDto(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentDate = payment.getCreatedDate();
    }
}

