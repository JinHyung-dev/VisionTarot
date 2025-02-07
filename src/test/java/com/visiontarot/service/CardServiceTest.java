package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.repository.CardRepository;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private CardService service;
    private final String SUMMARY = "할머니의 건강이 회복될 수 있을까요라는 고민에 대한 유니버셜 타로카드의 결과는 Nine of Swords 정방향입니다.";

    @Mock
    private GeminiApiRequestConfiguration geminiApiRequestConfiguration;

    @Autowired
    private CardRepository repository;

    @BeforeEach
    public void setUp() {
        service = new CardService(repository, geminiApiRequestConfiguration);
    }

    @Test
    public void 원카드_뽑기() {
        assertNotNull(service.drawOneCard());
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
