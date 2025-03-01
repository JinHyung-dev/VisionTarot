package com.visiontarot.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.visiontarot.dto.CardResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 한개뽑기_리턴CardRsponseDTO엔티티() {
        String concern = "나는 개발자로 성공할 수 있을까?";
        ResponseEntity<CardResponseDTO> response = restTemplate.postForEntity(
                "/card/onecard/draw-with-analyze",
                concern,
                CardResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getConcern()).isEqualTo(concern);
        assertThat(response.getBody().getGeminiAnswer()).isNotEmpty();
    }
}
