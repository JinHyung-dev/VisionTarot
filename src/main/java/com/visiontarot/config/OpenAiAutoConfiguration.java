package com.visiontarot.config;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value; // 필요할 경우 import
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiAutoConfiguration {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey; // application.properties 또는 환경 변수에 있는 API 키

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(apiKey); // API 키를 사용하여 OpenAiApi 생성
    }
    @Bean
    @ConditionalOnProperty(name = "spring.ai.openai.api-key", havingValue = "", matchIfMissing = false)
    public OpenAiChatModel openAiChatModel(OpenAiApi OpenAiApi) {
        return new OpenAiChatModel(OpenAiApi); // OpenAI API가 필요할 때만 빈을 생성
    }
}
