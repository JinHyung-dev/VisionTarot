package com.visiontarot.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.visiontarot.dto.GeminiResponseDTO;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeminiApiRequestConfigurationTest {
    private String mockJsonResponse;
    private String responseBody;

    @Spy
    private GeminiApiRequestConfiguration geminiApiClient = new GeminiApiRequestConfiguration();

    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    public void init() {
        mockJsonResponse = "{ \"candidates\": [{ \"content\": { \"parts\": [{ \"text\": \"예시 응답입니다.\" }] }, \"finishReason\": \"완료\" }] }";
        when(mockResponse.body()).thenReturn(mockJsonResponse);
    }

    @Test
    void 제미나이응답요청_httpResponse리턴() {
        String summary = "임시 결과";

//        when(geminiApiClient.makeRequest(summary)).thenReturn(mockResponse);
        doReturn(mockResponse).when(geminiApiClient).makeRequest(summary);

        HttpResponse<String> response = geminiApiClient.makeRequest(summary);
        responseBody = response.body();

        assertNotNull(response);
        assertEquals(mockJsonResponse, responseBody);
    }

    @Test
    void 제미나이응답파싱_GeminiResponseDTO리턴() {
        when(mockResponse.statusCode()).thenReturn(200);

        GeminiResponseDTO result = geminiApiClient.parseGeminiResponse(mockResponse);

        assertNotNull(result);
        assertEquals("예시 응답입니다.", result.getGeminiAnswer());
        assertEquals("완료", result.getFinishReason());
    }
}
