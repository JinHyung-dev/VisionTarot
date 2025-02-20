package com.visiontarot.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.service.CardService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardName")
                        .value("The Fool"));
    }
}
