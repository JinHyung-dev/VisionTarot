package com.visiontarot.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.CardService;
import com.visiontarot.service.ConcernCardService;
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

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

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
    void 카드1개뽑기() throws Exception {
        mockMvc.perform(get("/card/onecard/draw"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardName")
                        .value("The Fool"));
    }

    @Test
    void 카드뽑고_제미나이응답_고민카드업로드_CardResponseDTO전송() throws Exception {
        CardDTO card = new CardDTO(1L, "The Fool", "url", "imgName", "date", "date");
        String concern = "예시 고민입니다.";
        GeminiResponseDTO mockGeminiResponse = new GeminiResponseDTO("예시 응답입니다.", "완료", 200);
        BufferedImage mockImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        String mockImageUrl = "https://s3.amazonaws.com/sample-image.png";

        when(cardService.drawOneCard()).thenReturn(card);
        when(cardService.getGeminiResponse(concern, card)).thenReturn(mockGeminiResponse);
        when(concernCardService.createImage(any())).thenReturn(mockImage);
        when(concernCardService.uploadImageToS3(any(BufferedImage.class))).thenReturn(mockImageUrl);

        mockMvc.perform(post("/card/onecard/draw-with-concerncard").content(concern)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.card.cardName").value("The Fool"))
                .andExpect(jsonPath("$.concern").value(concern))
                .andExpect(jsonPath("$.geminiAnswer").value("예시 응답입니다."))
                .andExpect(jsonPath("$.concernCardImageUrl").value(mockImageUrl));

//        verify(concernCardService, times(1)).uploadImageToS3(any(BufferedImage.class));
    }
}
