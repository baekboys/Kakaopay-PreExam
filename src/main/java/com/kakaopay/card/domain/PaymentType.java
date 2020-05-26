package com.kakaopay.card.domain;

import lombok.Getter;

@Getter
public enum PaymentType {
    PAYMENT("1", "PAYMENT"),
    CANCEL("2", "CANCEL");

    String typeNum;
    String typeName;
    PaymentType(String typeNum, String typeName) {
        this.typeNum = typeNum;
        this.typeName = typeName;
    }
}

