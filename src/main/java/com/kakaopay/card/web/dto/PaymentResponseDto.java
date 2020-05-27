package com.kakaopay.card.web.dto;

import com.kakaopay.card.domain.payment.Payment;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class PaymentResponseDto extends ResponseDto {
    // 관리번호
    private String managementId = "";
    // 거래시간
    private String transactionTime = "";

    @Builder
    public PaymentResponseDto(String managementId, String transactionTime) {
        super();
        this.managementId = managementId;
        this.transactionTime = transactionTime;
    }
}

