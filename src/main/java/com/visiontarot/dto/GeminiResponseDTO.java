package com.visiontarot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponseDTO {
    private String geminiAnswer;
    private String finishReason;
    private int statusCode;
}
