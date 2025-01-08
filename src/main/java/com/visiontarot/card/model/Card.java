package com.visiontarot.card.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "universal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String cardName;
    private String cardImgUrl;
    private String cardImgName;
    private String cardCreateDate;
    private String cardUpdateDate;

    public CardDTO toDTO() {
        return new CardDTO(cardId, cardName, cardImgUrl, cardImgName, cardCreateDate, cardUpdateDate);
    }
}
