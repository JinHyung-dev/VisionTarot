package com.visiontarot.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.CardService;
import com.visiontarot.service.GeminiService;
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
    private GeminiService geminiService;

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
    void 카드뽑고제미나이응답_CardResponseDTO전송() throws Exception {
        CardDTO card = new CardDTO(1L, "The Fool", "url", "imgName", "date", "date");
        String concern = "예시 고민입니다.";
        GeminiResponseDTO mockGeminiResponse = new GeminiResponseDTO("예시 응답입니다.", "완료", 200);

        when(cardService.drawOneCard()).thenReturn(card);
        when(geminiService.getGeminiAnalyze(concern, card)).thenReturn(mockGeminiResponse);

        mockMvc.perform(post("/card/onecard/draw-with-analyze").content(concern)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.card.cardName").value("The Fool"))
                .andExpect(jsonPath("$.concern").value(concern))
                .andExpect(jsonPath("$.geminiAnswer").value("예시 응답입니다."));
    }
}
