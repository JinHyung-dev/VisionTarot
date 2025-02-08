package com.visiontarot.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GeminiApiRequestConfigurationTest {

    @Test
    void 제미나이_응답() {
    GeminiApiRequestConfiguration geminiApiClient = mock(GeminiApiRequestConfiguration.class);

    String summary = "임시 결과";
    Map<String, Object> testMap = new HashMap<>();
    testMap.put("Response Code", 200);
    testMap.put("Response Body", "성공");

    when(geminiApiClient.makeRequest(summary)).thenReturn(testMap);

    Map<String, Object> response = geminiApiClient.makeRequest(summary);

    assertThat(response.get("Response Code")).isEqualTo(200);
    assertThat(response.get("Response Body")).isEqualTo("성공");
    }
}
