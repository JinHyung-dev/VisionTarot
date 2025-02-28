package com.visiontarot.config;

import com.visiontarot.dto.GeminiResponseDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GeminiApiRequestConfiguration {

    @Value("${spring.ai.vertex.ai.gemini.api-endpoint}")
    private String apiEndpoint;

    @Value("${spring.ai.vertex.ai.gemini.api-key}")
    private String apiKey;

    public GeminiResponseDTO getGeminiResponse(String userConcernAndCardResult) {
        return parseGeminiResponse(makeRequest(userConcernAndCardResult));
    }

    public HttpResponse<String> makeRequest(String summary) {
        HttpResponse<String> result = null;
        try {
            // URL 및 API 키 설정
            String url = apiEndpoint + "?key=" + apiKey;
            log.info(">>> 제미나이 응답 요청 발송 url : {}", url);

            // JSON 요청 본문
            String jsonInputString = "{"
                    + "\"contents\": [{"
                    + "\"parts\": [{"
                    + "\"text\": \"" + summary + "\""
                    + "}]"
                    + "}]"
                    + "}";
            log.info(">>> 제미나이 응답 요청 발송 : {}", jsonInputString);

            // HttpClient 객체 생성
            HttpClient client = HttpClient.newHttpClient();

            // HttpRequest 객체 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                    .build();

            // 요청 보내고 응답 받기
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            log.info("API 호출 중 오류 발생{}", e.getMessage());
        }
        return result;
    }

    public GeminiResponseDTO parseGeminiResponse(HttpResponse<String> response) {
        log.info(">>> 제미나이 응답 파싱 진행");
        String responseBody = response.body();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONObject firstCandidate = jsonResponse.getJSONArray("candidates").getJSONObject(0);

        String geminiAnswer = firstCandidate.getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
        String finishReason = firstCandidate.getString("finishReason");
        int statusCode = response.statusCode();
        log.info(">>> 제미나이 응답 : {}", geminiAnswer);
        log.info(">>> 제미나이 status Code : {}", statusCode);

        return new GeminiResponseDTO(geminiAnswer, finishReason, statusCode);
    }
}
