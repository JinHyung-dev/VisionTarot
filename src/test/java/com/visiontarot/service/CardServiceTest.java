package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.domain.Card;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.repository.CardRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private CardService service;
    private final String SUMMARY = "할머니의 건강이 회복될 수 있을까요라는 고민에 대한 유니버셜 타로카드의 결과는 Nine of Swords 정방향입니다.";

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

    @Test
    public void 제미나이_응답받기() {
        Map<String, Object> result = new HashMap<>();
        result.put("Response Code", 200);
        result.put("Response Body", "성공");

        when(geminiApiRequestConfiguration.makeRequest(SUMMARY)).thenReturn(result);
        Assertions.assertThat(geminiApiRequestConfiguration.makeRequest(SUMMARY)).isNotNull();
        Assertions.assertThat(result.get("Response Code")).isEqualTo(200);
    }
}
