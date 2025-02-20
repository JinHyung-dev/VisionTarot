package com.visiontarot.dto;

import java.util.Objects;
import java.util.Random;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Data
public class CardDTO {
    private Long id;
    private String cardName;
    private boolean isReversed; // 0이면 정방향, 1이면 역방향
    private String cardImgUrl;
    private String cardImgName;
    private final String STATICURL = "/images/universal/";

    public CardDTO() {
    }

    public CardDTO(Long cardId, String cardName, String cardImgUrl, String cardImgName, String cardCreateDate, String cardUpdateDate) {
        this.id = cardId;
        this.cardName = cardName;
        this.cardImgName = cardImgName;
        this.cardImgUrl = Objects.requireNonNullElseGet(cardImgUrl, () -> STATICURL + cardImgName + ".jpg");
    }

    public void assignRandomDirection() {
        this.isReversed = new Random().nextBoolean();
    }
}
