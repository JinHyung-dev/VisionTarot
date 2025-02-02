package com.visiontarot.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.visiontarot.domain.Card;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardRepositoryTest {
    @Autowired
    private CardRepository repository;

    @Test
    public void 카드덱_가져오기() {
        List<Card> cards = repository.findAll();
        assertFalse(cards.isEmpty());
    }
}
