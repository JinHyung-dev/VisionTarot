package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.domain.Card;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.repository.CardRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private CardService service;

    @Mock
    private GeminiApiRequestConfiguration geminiApiRequestConfiguration;

    @Mock
    private CardRepository repository;

    @BeforeEach
    public void setUp() {
        service = new CardService(repository, geminiApiRequestConfiguration);
    }

    @Test
    public void 원카드_뽑기() {
        List<Card> mockCards = Arrays.asList(
                new Card(1L, "The Fool"),
                new Card(2L, "The Magician")
        );
        when(repository.findAll()).thenReturn(mockCards);
        CardDTO result = service.drawOneCard();
        assertNotNull(result);
    }
}
