package com.visiontarot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CardResponseDTO {
    private CardDTO card;
    private String concern;
    private String geminiAnswer; //GeminiResponseDTO: geminiAnswer
    private String concernCardImageUrl;
}
