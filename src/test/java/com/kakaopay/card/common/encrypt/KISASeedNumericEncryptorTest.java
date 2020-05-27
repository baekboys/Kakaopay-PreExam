package com.kakaopay.card.common.encrypt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KISASeedNumericEncryptorTest {

    @Test
    public void paymentSave() {
        //given
        String text = "KakaopayPreExam";

        //when
        String enc_text = KISASeedNumericEncryptor.encrypt(text);
        String dec_text = KISASeedNumericEncryptor.decrypt(enc_text);

        //then
        assertThat(text).isNotEqualTo(enc_text);
        assertThat(text).isEqualTo(dec_text);
    }
}
