package com.kakaopay.card.common;

import com.kakaopay.card.domain.PaymentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ManagementIdGeneratorTest {

    @Test
    public void autoGenMngIdTest() {
        //given
        String cardNumber1 = "4321432143214321";
        PaymentType type1 = PaymentType.PAYMENT;

        String cardNumber2 = "4321432143214321";
        PaymentType type2 = PaymentType.CANCEL;

        String cardNumber3 = "4321432143214322";
        PaymentType type3 = PaymentType.PAYMENT;

        //when
        String managementId1 = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(type1, cardNumber1);
        String managementId2 = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(type1, cardNumber1);
        String managementId3 = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(type1, cardNumber1);

        //then
        assertThat(managementId1.length()).isEqualTo(20);
        assertThat(managementId2.length()).isEqualTo(20);
        assertThat(managementId3.length()).isEqualTo(20);

        assertThat(managementId1).isNotEqualTo(managementId2);
        assertThat(managementId2).isNotEqualTo(managementId3);
        assertThat(managementId3).isNotEqualTo(managementId1);
    }

    @Test
    public void encodeAndDecodeTest() {
        //given
        String cardNumber1 = "4321432143214321";
        PaymentType type1 = PaymentType.PAYMENT;

        //when
        String managementId1 = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(type1, cardNumber1);
        String decodeNum = ManagementIdGenerator.getBase10From62(managementId1);
        String decodeCardNum = decodeNum.substring(2, 2+16);

        //then
        assertThat(managementId1.length()).isEqualTo(20);
        assertThat(decodeCardNum).isEqualTo(cardNumber1);
    }
}
