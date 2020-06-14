package com.kakaopay.card.web;

import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.Exception.CancelBizException;
import com.kakaopay.card.Exception.PaymentBizException;
import com.kakaopay.card.Exception.SearchBizException;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class ApiController {

    private final PaymentService paymentService;

    /****************************************************
     * 결제 진행 프로세스
     * @param paymentRequestDto
     * @return
     ****************************************************/
    @PutMapping(value="/payment")
    public PaymentResponseDto payment(@RequestBody PaymentRequestDto paymentRequestDto) {

        try {
            // 유효성 체크
            if(!paymentRequestDto.isValid()) {
                throw new PaymentBizException(BizExceptionType.INVALID_PARAM);
            }
        } catch (PaymentBizException pbe) {
            throw pbe;
        } catch (Exception e) {
            throw e;
        }

        // 결제 진행
        PaymentResponseDto paymentResponseDto = null;
        try {
            paymentResponseDto = paymentService.save(paymentRequestDto);
            if(paymentResponseDto == null) {
                throw new PaymentBizException(BizExceptionType.PAYMENT_NOT_PROCESS);
            }
        } catch (PaymentBizException pbe) {
            throw pbe;
        } catch (Exception e) {
            throw e;
        }
        return paymentResponseDto;
    }

    /****************************************************
     * 관리번호로 결제/취소정보 조회
     * @param managementId
     * @return
     ****************************************************/
    @GetMapping("/search/{managementId}")
    public SearchResponseDto searchFromManagementId(@PathVariable String managementId) {

        // 관리번호가 비어있거나 길이가 20이 아니면 유효값이 아님
        if( StringUtils.isEmpty(managementId) || managementId.length() != 20  ) {
            throw new SearchBizException(BizExceptionType.INVALID_MANAGEMENT_ID);
        }

        // 관리번호로 결제/취소정보 조회
        SearchResponseDto searchResponseDto = null;
        try {
            searchResponseDto  = paymentService.search(managementId);

            if(searchResponseDto == null) {
                throw new SearchBizException(BizExceptionType.DATA_NOT_FONUD);
            }
        } catch (SearchBizException sbe) {
            throw sbe;
        } catch (Exception e) {
            throw e;
        }


        return searchResponseDto;
    }

    /****************************************************
     * 관리번호로 취소처리
     * @param cancelReqeustDto
     * @return
     ****************************************************/
    @PostMapping("/cancel")
    public CancelResponseDto cancel(@RequestBody CancelReqeustDto cancelReqeustDto) {

        try {
            // 유효성 체크
            if(!cancelReqeustDto.isValid()) {
                throw new CancelBizException(BizExceptionType.INVALID_PARAM);
            }
        } catch (CancelBizException cbe) {
            throw cbe;
        } catch (Exception e) {
            throw e;
        }

        // 관리번호로 취소처리
        String managementId = cancelReqeustDto.getManagementId();
        CancelResponseDto cancelResponseDto = null;
        try {
            cancelResponseDto = paymentService.cancel(cancelReqeustDto);
            if(cancelResponseDto == null) {
                throw new CancelBizException(BizExceptionType.CANCEL_NOT_PROCESS);
            }
        } catch (CancelBizException cbe) {
            throw cbe;
        } catch (Exception e) {
            throw e;
        }

        return cancelResponseDto;
    }
}