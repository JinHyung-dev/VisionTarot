package com.visiontarot.card.model;

import java.util.Objects;
import java.util.Random;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CardDTO {
    private Long id;
    private String cardName;
    private final boolean isReversed; // 0이면 정방향, 1이면 역방향
    private String cardImgUrl;
    private String cardImgName;
    private final String STATICURL = "/images/universal/";

    public CardDTO() {
        Random random = new Random();
        isReversed = random.nextBoolean();
    }

    public CardDTO(Long cardId, String cardName, String cardImgUrl, String cardImgName, String cardCreateDate, String cardUpdateDate) {
        this.id = cardId;
        this.cardName = cardName;
        this.cardImgName = cardImgName;
        this.cardImgUrl = Objects.requireNonNullElseGet(cardImgUrl, () -> STATICURL + cardImgName + ".jpg");
        Random random = new Random();
        this.isReversed = random.nextBoolean();
    }
}
