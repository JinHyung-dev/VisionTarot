package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeminiServiceTest {
    private GeminiService geminiService;

    private String mockJsonResponse;

    @Mock
    private HttpResponse<String> mockResponse;

    @Spy
    private GeminiApiRequestConfiguration geminiConfig;

    @BeforeEach
    public void setUp() {
        geminiService = new GeminiService(geminiConfig);
        mockJsonResponse = "{ \"candidates\": [{ \"content\": { \"parts\": [{ \"text\": \"예시 응답입니다.\" }] }, \"finishReason\": \"완료\" }] }";
    }

    @Test
    public void 제미나이분석결과조회_GeminiResponseDTO리턴() {
        String concern = "이것은 임시 고민입니다.";
        CardDTO cardDTO = new CardDTO(1L, "The Fool", "imageurl", "imagename", "2025-01-01", "2025-01-01");

        doReturn(mockResponse).when(geminiConfig).makeRequest(anyString());
        when(mockResponse.body()).thenReturn(mockJsonResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        GeminiResponseDTO result = geminiService.getGeminiAnalyze(concern, cardDTO);

        assertNotNull(result);
        assertEquals("예시 응답입니다.", result.getGeminiAnswer());
        assertEquals("완료", result.getFinishReason());
    }

    @Test
    public void 제미나이요약조회_GeminiResponseDTO리턴() {
        String prevAnswer = "이전 응답 예시입니다.";
        doReturn(mockResponse).when(geminiConfig).makeRequest(anyString());
        when(mockResponse.body()).thenReturn(mockJsonResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        GeminiResponseDTO result = geminiService.getGeminiSummary(prevAnswer);

        assertNotNull(result);
        assertEquals("예시 응답입니다.", result.getGeminiAnswer());
        assertEquals("완료", result.getFinishReason());

    }
}
