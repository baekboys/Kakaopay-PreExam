package com.kakaopay.card.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
public class PaymentRequestDto implements RequestDto {
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


    @Override
    public boolean isValidate() {

        if( StringUtils.isEmpty(cardnum) || !(cardnum.length() >= 10 && cardnum.length() <= 16) || !StringUtils.isNumeric(cardnum)  ) {
            return false;
        }

        if( StringUtils.isEmpty(expired) || expired.length() != 4 || ! StringUtils.isNumeric(expired) ) {
            return false;
        }

        if( StringUtils.isEmpty(cvc) || cvc.length() != 3 || ! StringUtils.isNumeric(cvc) ) {
            return false;
        }

        if( StringUtils.isEmpty(installment) || !(installment.length() >= 1 && installment.length() <= 2) || ! StringUtils.isNumeric(installment) ) {
            return false;
        }

        if( StringUtils.isEmpty(amount) || amount.length() > 10 || ! StringUtils.isNumeric(amount) ) {
            return false;
        }

        if( !StringUtils.isEmpty(vat) && ( vat.length() > 10 || ! StringUtils.isNumeric(vat) ) ) {
            return false;
        }

        if( !StringUtils.isEmpty(amount) && !StringUtils.isEmpty(vat) ) {
            long requestAmount = Long.valueOf(amount);
            long requestVat = Long.valueOf(vat);

            if(requestVat > requestAmount) {
                return false;
            }
        }

        return true;
    }
}

