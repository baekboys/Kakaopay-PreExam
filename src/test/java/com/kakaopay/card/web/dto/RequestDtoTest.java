package com.kakaopay.card.web.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestDtoTest {

    @Test
    public void validationTest() {
        //given
        String cardnum = "1234567890123456";
        String expired = "1234";
        String cvc = "321";
        String installment = "03";
        String amount = "13350000";
        String vat = "10000";

        //when
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //then
        assertThat(paymentRequestDto.isValid()).isFalse();
    }

    @Test
    public void validationVatOkTest() {
        //given
        String cardnum = "1234567890123456";
        String expired = "1234";
        String cvc = "321";
        String installment = "03";
        String amount = "13350000";
        String vat = "";

        //when
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //then
        assertThat(paymentRequestDto.isValid()).isTrue();
    }

    @Test
    public void validationVatFailTest() {
        //given
        String cardnum = "1234567890123456";
        String expired = "1234";
        String cvc = "321";
        String installment = "03";
        String amount = "13350000";
        String vat = "A1";

        //when
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //then
        assertThat(paymentRequestDto.isValid()).isFalse();
    }

    @Test
    public void validationVatOverFailTest() {
        //given
        String cardnum = "1234567890123456";
        String expired = "1234";
        String cvc = "321";
        String installment = "03";
        String amount = "10000";
        String vat = "10001";

        //when
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //then
        assertThat(paymentRequestDto.isValid()).isFalse();
    }
}
