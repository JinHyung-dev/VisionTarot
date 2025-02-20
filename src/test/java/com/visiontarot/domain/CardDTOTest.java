package com.visiontarot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.visiontarot.dto.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardDTOTest {
    private CardDTO cardDTO;

    @BeforeEach
    public void setUp() {
        cardDTO = new CardDTO(1L, "The Fool", null, "Major00", "2021-01-01", "2021-01-02");
    }

    @Test
    public void 카드주소_세팅하기() {
        assertEquals("/images/universal/Major00.jpg", cardDTO.getCardImgUrl());
    }
}
