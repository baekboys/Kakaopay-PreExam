package com.kakaopay.card.domain;

import lombok.Getter;

@Getter
public enum PaymentType {
    PAYMENT("1", "PAYMENT", "결제"),
    CANCEL("2", "CANCEL", "취소");

    String typeNum;
    String typeName;
    String typeKorName;
    PaymentType(String typeNum, String typeName, String typeKorName) {
        this.typeNum = typeNum;
        this.typeName = typeName;
        this.typeKorName = typeKorName;
    }
}

