package com.kakaopay.card.common.encrypt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KISASeedEncryptorTest {

    @Test
    public void paymentSave() {
        //given
        String text = "KakaopayPreExam";

        //when
        String enc_text = KISASeedEncryptor.encrypt(text);
        String dec_text = KISASeedEncryptor.decrypt(enc_text);

        //then
        assertThat(text).isNotEqualTo(enc_text);
        assertThat(text).isEqualTo(dec_text);
    }
}
