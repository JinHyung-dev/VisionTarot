package com.visiontarot.service;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeminiService {
    private final GeminiApiRequestConfiguration geminiApiRequestConfiguration;

    @Autowired
    public GeminiService(GeminiApiRequestConfiguration geminiApiRequestConfiguration) {
        this.geminiApiRequestConfiguration = geminiApiRequestConfiguration;
    }

    public GeminiResponseDTO getGeminiAnalyze(String concern, CardDTO card) {
        String userConcernAndCardResult = createGeminiRequestAnalyze(concern, card);
        log.info(">>> 다음 summary에 대해 제미나이 분석을 요청합니다.\n[{}]", userConcernAndCardResult);
        return geminiApiRequestConfiguration.getGeminiResponse(userConcernAndCardResult);
    }

    private String createGeminiRequestAnalyze(String concern, CardDTO card) {
        if (concern == null || card == null || card.getCardName() == null) {
            return "잘못된 입력입니다. 고민 내용과 카드 정보를 다시 확인해주세요.";
        }
        log.info(">>> 고민과 카드뽑기 내용을 조합한 질문 생성");
        return "현재 고민에 대한 유니버셜 타로카드 1카드 뽑기를 진행했어."
                + "고민: " + concern
                + "뽑기 결과: " + card.getCardName() + "카드가 "
                + (card.isReversed() ? "역방향" : "정방향")
                +"으로 나왔어. 한국말로 이 상황에서 이 카드가 무슨 의미일지 해석해줘.(부호나 기호 사용 금지)";
    }

    public GeminiResponseDTO getGeminiSummary(String prevAnswer) {
        String geminiRequestSummary = createGeminiRequestSummary(prevAnswer);
        log.info(">>> 다음 summary에 대해 제미나이 요약을 요청합니다.\n[{}]", geminiRequestSummary);
        return geminiApiRequestConfiguration.getGeminiResponse(geminiRequestSummary);
    }

    private String createGeminiRequestSummary(String prevAnswer) {
        if (prevAnswer == null) {
            return "잘못된 요청입니다. 이전 응답 내용을 다시 확인해주세요.";
        }
        log.info(">>> 고민과 카드뽑기 내용을 요약한 summary 요청문");
        return "네가 주었던 응답 내용을 200자로 요약해주고, 그 뒤에 격려나 위로의 한 문장을 덧붙여줘.(문장 간 띄어쓰기는 1번만 허용, 엔터 금지) "
                + "이전 응답 : " + prevAnswer;
    }
}
