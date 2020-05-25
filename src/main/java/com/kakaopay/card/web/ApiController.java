package com.kakaopay.card.web;

import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ApiController {

    private final PaymentService paymentService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

 /*   @PostMapping("/api/v1/posts")
    public CardInfo save(@RequestBody CardInfoSaveRequestDto requestDto) {
        CardInfo cardInfo =  cardInfoService.save(requestDto);
        return cardInfo;
    }*/

    @PostMapping("/api/v1/payment")
    public PaymentResponseDto payment(@RequestBody PaymentRequestDto requestDto) {

        Payment payment  = paymentService.save(requestDto);

        // 추가적인 데이터가 있을 수 있으므로 결제번호(paymentId) 대신 paymentResponseDto를 리턴
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(payment);

        return paymentResponseDto;
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    /*@GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable long id, Model model) {

        PostsResponseDto dto = postService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }*/
}