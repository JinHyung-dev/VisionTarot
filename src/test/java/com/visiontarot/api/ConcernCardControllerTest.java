package com.visiontarot.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.CardService;
import com.visiontarot.service.ConcernCardService;
import com.visiontarot.service.GeminiService;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(ConcernCardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConcernCardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private GeminiService geminiService;

    @MockBean
    private ConcernCardService concernCardService;

    @BeforeEach
    public void setUp() {
        List<CardDTO> mockCards = Arrays.asList(
                new CardDTO(1L, "The Fool", "url", "imgName", "date", "date"),
                new CardDTO(2L, "The Magician", "url", "imgName", "date", "date")
        );

        when(cardService.drawOneCard()).thenReturn(mockCards.get(0));
    }
    @Test
    void 고민카드생성_CardResponseDTO전송() throws Exception {
        String prevGeminiAnswer = "예시 제미나이 분석입니다.";
        GeminiResponseDTO mockGeminiResponse = new GeminiResponseDTO("예시 요약 내용입니다.", "완료", 200);
        BufferedImage mockImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        String mockImageUrl = "https://s3.amazonaws.com/sample-image.jpg";

        when(geminiService.getGeminiSummary(prevGeminiAnswer)).thenReturn(mockGeminiResponse);
        when(concernCardService.createImage(any())).thenReturn(mockImage);
        when(concernCardService.uploadImageToS3(any(BufferedImage.class))).thenReturn(mockImageUrl);

        mockMvc.perform(post("/concern-card/create")
                        .content(prevGeminiAnswer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.geminiAnswer").value("예시 요약 내용입니다."))
                .andExpect(jsonPath("$.concernCardImageUrl").value(mockImageUrl));
    }
}
