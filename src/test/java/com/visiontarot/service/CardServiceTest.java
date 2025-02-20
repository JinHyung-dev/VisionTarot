package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.domain.Card;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.repository.CardRepository;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private CardService service;
    private String mockJsonResponse;

    @Spy
    private GeminiApiRequestConfiguration geminiConfig;

    @Mock
    private CardRepository repository;

    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    public void setUp() {
        service = new CardService(repository, geminiConfig);
        mockJsonResponse = "{ \"candidates\": [{ \"geminiAnswer\": \"예시 응답입니다.\", \"finishReason\": \"완료\" }] }";
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
    public void 제미나이분석결과조회_GeminiResponseDTO리턴() {
        String concern = "이것은 임시 고민입니다.";
        CardDTO cardDTO = new CardDTO(1L, "The Fool", "imageurl", "imagename", "2025-01-01", "2025-01-01");

        doReturn(mockResponse).when(geminiConfig).makeRequest(anyString());
        when(mockResponse.body()).thenReturn(mockJsonResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        GeminiResponseDTO result = service.getGeminiResponse(concern, cardDTO);

        assertNotNull(result);
        assertEquals("예시 응답입니다.", result.getGeminiAnswer());
        assertEquals("완료", result.getFinishReason());
    }
}
