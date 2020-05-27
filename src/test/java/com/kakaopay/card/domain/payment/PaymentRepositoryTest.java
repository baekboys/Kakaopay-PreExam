package com.kakaopay.card.domain.payment;

import com.kakaopay.card.domain.PaymentType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

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
    public void paymentSaveTest() {
        //given
        PaymentType paymentType = PaymentType.PAYMENT;

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "ZZZZZZZZZZZZZZZZZZZZ";

        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        Payment paymentResult = paymentRepository.save(Payment.builder()
                .paymentType(paymentType)
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());
        //when
        List<Payment> paymentList = paymentRepository.findAll();
        Payment paymentGetObj = paymentList.get(0);

        //then
        assertThat(paymentResult.getPaymentType()).isEqualTo(paymentType);
        assertThat(paymentResult.getPaymentId()).isEqualTo(paymentId);
        assertThat(paymentResult.getCancelId()).isEqualTo(cancelId);
        assertThat(paymentResult.getCardnum()).isEqualTo(cardnum);
        assertThat(paymentResult.getExpired()).isEqualTo(expired);
        assertThat(paymentResult.getCvc()).isEqualTo(cvc);
        assertThat(paymentResult.getInstallment()).isEqualTo(installment);
        assertThat(Long.toString(paymentResult.getAmount())).isEqualTo(amount);
        assertThat(Long.toString(paymentResult.getVat())).isEqualTo(vat);
    }

    @Test
    public void paymentSearchTest() {

        //given
        PaymentType paymentType = PaymentType.PAYMENT;

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "ZZZZZZZZZZZZZZZZZZZZ";

        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        String managementId1 = "ZZZZZZZZZZZZZZZZZZZZ";
        String managementId2 = "XXXXXXXXXXXXXXXXXXXX";


        Payment paymentResult = paymentRepository.save(Payment.builder()
                .paymentType(paymentType)
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());
        //when
        Optional<Payment> paymentResult1 = paymentRepository.findByPaymentId(managementId1);
        Optional<Payment> paymentResult2 = paymentRepository.findByPaymentId(managementId2);
        Optional<Payment> paymentResult3 = paymentRepository.findByCancelId(managementId1);
        Optional<Payment> paymentResult4 = paymentRepository.findByCancelId(managementId2);

        //then
        assertThat(paymentResult1.isPresent()).isFalse();
        assertThat(paymentResult2.isPresent()).isTrue();
        assertThat(paymentResult3.isPresent()).isTrue();
        assertThat(paymentResult4.isPresent()).isFalse();
    }
}
