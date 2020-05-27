package com.kakaopay.card.domain.cardinfo;

import com.kakaopay.card.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class CardInfo extends BaseTimeEntity {

    // 관리번호
    @Id
    @Column(length = 20, nullable = false)
    String managementId;

    // 카드사 저장 데이터
    @Column(length = 450, nullable = false)
    String data;

    @Builder
    public CardInfo(String managementId, String data) {
        this.managementId = managementId;
        this.data = data;
    }
}
