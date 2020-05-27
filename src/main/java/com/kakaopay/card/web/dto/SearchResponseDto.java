package com.kakaopay.card.web.dto;

import com.kakaopay.card.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class SearchResponseDto extends ResponseDto {
    // 관리번호
    private String managementId = "";

    // 카드번호
    private String cardNum = "";
    // 유효기간
    private String expired = "";
    // CVC
    private String cvc = "";

    // 결제/취소 구분
    private String paymentType = "";
    // 금액
    private String amount = "";
    // 부가가치세
    private String vat = "";

    // 거래시간
    private String transactionTime = "";

    @Builder
    public SearchResponseDto(String managementId, String cardNum, String expired, String cvc, String paymentType, String amount, String vat, String transactionTime) {
        super();
        this.managementId = managementId;
        this.cardNum = cardNum;
        this.expired = expired;
        this.cvc = cvc;
        this.paymentType = paymentType;
        this.amount = amount;
        this.vat = vat;
        this.transactionTime = transactionTime;
    }
}

