package com.kakaopay.card.service.payment;

import com.kakaopay.card.common.ManagementIdGenerator;
import com.kakaopay.card.common.PaymentToCardInfoData;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.cardinfo.CardInfo;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.web.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    // 결제정보 레파지토리
    private final PaymentRepository paymentRepository;

    // 카드사정보 레파지토리
    private final CardInfoRepository cardInfoRepository;

    @Transactional
    public Payment save(PaymentRequestDto requestDto) {
        //---------------------------------------------------------------------------
        // Request로 부터 전달받은 결제정보로 관리번호 채번
        //---------------------------------------------------------------------------
        // PaymentRequestDto 값 세팅
        String cardnum = requestDto.getCardnum();
        String expired = requestDto.getExpired();
        String cvc = requestDto.getCvc();
        String amount = requestDto.getAmount();
        String installment  = requestDto.getInstallment();
        String vat = requestDto.getVat();

        // 결제타입이 결제(PAYMENT)이면서 유니크한 카드번호로 부터 관리번호 20자리 채번
        String paymentId = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(PaymentType.PAYMENT, cardnum);

        //---------------------------------------------------------------------------
        // 결제정보 저장 프로세스
        //---------------------------------------------------------------------------
        // 결제정보를 저장 할 Payment 객체 생성
        Payment newPayment = Payment.builder()
                .paymentId(paymentId)
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .amount(amount)
                .installment(installment)
                .vat(vat)
                .build()
                ;

        // 결제정보 저장
        Payment savePayment = paymentRepository.save(newPayment);

        //---------------------------------------------------------------------------
        // 카드사 정보 저장 프로세스
        //---------------------------------------------------------------------------
        // 카드사 정보 문자열 생성
        String cardinfoData = PaymentToCardInfoData.doChangePaymentToCardInfoData(savePayment);

        // 관리번호와 카드사 정보를 저장할 CardInfo 객체 생성
        CardInfo newCardInfo = CardInfo
                .builder()
                .paymentId(paymentId)
                .data(cardinfoData)
                .build();

        // 카드사 정보 저장
        CardInfo saveCardInfo = cardInfoRepository.save(newCardInfo);

        return savePayment;
    }
}
