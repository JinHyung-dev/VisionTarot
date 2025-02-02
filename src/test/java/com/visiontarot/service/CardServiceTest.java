package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.visiontarot.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardServiceTest {
    private CardService service;
    @Autowired
    private CardRepository repository;

    @BeforeEach
    public void setUp() {
        service = new CardService(repository);
    }

    @Test
    public void 원카드_뽑기() {
        assertNotNull(service.drawOneCard());
    }
}
