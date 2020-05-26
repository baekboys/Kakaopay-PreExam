package com.kakaopay.card.web.dto;

import com.kakaopay.card.domain.payment.Payment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponseDto extends ResponseDto {
    private String paymentId = "";
    private LocalDateTime paymentDate;

    @Builder
    public PaymentResponseDto(Payment payment) {
        super();
        this.paymentId = payment.getPaymentId();
        this.paymentDate = payment.getCreatedDate();
    }
}

