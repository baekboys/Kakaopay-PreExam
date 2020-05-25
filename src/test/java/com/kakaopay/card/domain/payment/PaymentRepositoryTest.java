package com.kakaopay.card.domain.payment;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @After
    public void cleanup() {
        paymentRepository.deleteAll();
    }

    @Test
    public void paymentSave() {
        //given
        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        Payment paymentResult = paymentRepository.save(Payment.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());

        System.out.println("paymentResult :" + paymentResult.getCardnum());

        //when
        List<Payment> paymentList = paymentRepository.findAll();

        //then
        Payment paymentGetObj = paymentList.get(0);
        assertThat(paymentResult.getCardnum()).isEqualTo(cardnum);
        assertThat(paymentResult.getExpired()).isEqualTo(expired);
        assertThat(paymentResult.getCvc()).isEqualTo(cvc);
        assertThat(paymentResult.getInstallment()).isEqualTo(installment);
        assertThat(paymentResult.getVat()).isEqualTo(vat);
    }
}
