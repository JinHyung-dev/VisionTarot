package com.visiontarot.config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiApiRequestConfiguration {
    @Value("${spring.ai.vertex.ai.gemini.api-endpoint}")
    private String apiEndpoint;

    @Value("${spring.ai.vertex.ai.gemini.api-key}")
    private String apiKey;

    public Map<String, Object> makeRequest(String summary) {
        Map<String, Object> result = new HashMap<>();
        try {
            // URL 및 API 키 설정
            String url = apiEndpoint + "?key=" + apiKey;

            // JSON 요청 본문
            String jsonInputString = "{"
                    + "\"contents\": [{"
                    + "\"parts\": [{"
                    + "\"text\": " + summary
                    + "}]"
                    + "}]"
                    + "}";

            // HttpClient 객체 생성
            HttpClient client = HttpClient.newHttpClient();

            // HttpRequest 객체 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                    .build();

            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 코드 및 응답 내용 출력
            result.put("Response Code", response.statusCode());
            result.put("Response Body", response.body());

        } catch (Exception e) {
            e.printStackTrace();
            result.put("Error", "API 호출 중 오류 발생");
        }

        return result;
    }
}
