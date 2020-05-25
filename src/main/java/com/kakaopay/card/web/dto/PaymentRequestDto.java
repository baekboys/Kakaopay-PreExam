package com.kakaopay.card.web.dto;

import com.kakaopay.card.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequestDto {
    private String cardnum;
    private String expired;
    private String cvc;
    private String installment;
    private String amount;
    private String vat;

    @Builder
    public PaymentRequestDto(String cardnum, String expired, String cvc, String installment, String amount, String vat) {
        this.cardnum = cardnum;
        this.expired = expired;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = amount;
        this.vat = vat;
    }
}

