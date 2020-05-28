package com.kakaopay.card.web.dto;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.Exception.PaymentBizException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
public class PaymentRequestDto implements RequestDto {
    // 카드번호
    private String cardnum;
    // 유효기간
    private String expired;
    // CVCV
    private String cvc;
    // 할부
    private String installment;
    // 결제금액
    private String amount;
    // 부가가치세
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
    public boolean isValid() throws PaymentBizException {

        boolean isValid = true;

        if( StringUtils.isEmpty(cardnum) || !(cardnum.length() >= 10 && cardnum.length() <= 16) || !StringUtils.isNumeric(cardnum)  ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_CARD_NUM);
        }

        if( StringUtils.isEmpty(expired) || expired.length() != 4 || !StringUtils.isNumeric(expired) || !( Integer.valueOf(expired.substring(0, 2))  >= 1 && Integer.valueOf(expired.substring(0, 2)) <= 12 ) ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_EXPIERD_NUM);
        }

        if( StringUtils.isEmpty(cvc) || cvc.length() != 3 || !StringUtils.isNumeric(cvc) ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_CVC_NUM);
        }

        if( StringUtils.isEmpty(installment) || !StringUtils.isNumeric(installment) || !( Integer.valueOf(installment)  >= 0 && Integer.valueOf(installment) <= 12 )  ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_INSTALL_NUM);
        }


        if( StringUtils.isEmpty(amount) || !StringUtils.isNumeric(amount) || !( Long.valueOf(amount)  >= 100L && Long.valueOf(amount) <= 1000000000L ) ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_AMOUNT_NUM);
        }

        if( !( Long.valueOf(amount)  >= 100L && Long.valueOf(amount) <= 1000000000L ) ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_AMOUNT_EXCEED);
        }

        if( !StringUtils.isEmpty(vat) && ( vat.length() > 10 || ! StringUtils.isNumeric(vat) ) ) {
            isValid = false;
            throw new PaymentBizException(BizExceptionType.INVALID_VAT_NUM);
        }

        if( !StringUtils.isEmpty(amount) && !StringUtils.isEmpty(vat) ) {
            long requestAmount = Long.valueOf(amount);
            long requestVat = Long.valueOf(vat);

            if(requestVat > requestAmount) {
                isValid = false;
                throw new PaymentBizException(BizExceptionType.INVALID_VAT_EXCEED);
            }
        }

        return isValid;
    }
}

