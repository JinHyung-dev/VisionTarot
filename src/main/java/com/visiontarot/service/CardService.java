package com.visiontarot.service;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.domain.Card;
import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.repository.CardRepository;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardService {
    private List<CardDTO> cardDTOS;
    private final CardRepository cardRepository;
    private final GeminiApiRequestConfiguration geminiApiRequestConfiguration;

    @Autowired
    public CardService(CardRepository cardRepository, GeminiApiRequestConfiguration geminiApiRequestConfiguration) {
        this.cardRepository = cardRepository;
        this.geminiApiRequestConfiguration = geminiApiRequestConfiguration;
    }

    public CardDTO drawOneCard() {
        log.info(">>> 유니버셜 카드덱 데이터 확인 <<<");
        if (cardDTOS == null || cardDTOS.isEmpty()) { // 카드 정보가 없다면 조회
            log.info("유니버셜 카드덱 데이터가 없으므로 사전 작업을 수행합니다.");
            this.cardDTOS = getAllCards();
        }

        log.info(">>> 1카드 뽑기 실행 <<<");
        Random random = new Random();
        CardDTO card = cardDTOS.get(random.nextInt(cardDTOS.size()));
        card.assignRandomDirection();  // 카드 방향 결정
        log.info("결과 : {}", card);
        return card;
    }

    public List<CardDTO> getAllCards() {
        log.info(">>> 사전 작업 : 유니버셜 카드덱 데이터 불러오기 <<<");
        List<Card> cards = cardRepository.findAll();
        log.info(">>> 사전 작업 완료 <<<");
        log.info("# 전체 카드덱 : {}", cards);
        return cards.stream()
                .map(Card::toDTO)  // 엔티티 -> DTO로 변환
                .collect(Collectors.toList());
    }

    public GeminiResponseDTO getGeminiResponse(String concern, CardDTO card) {
        String userConcernAndCardResult = createGeminiRequestString(concern, card);
        log.info(">>> 다음 summary에 대해 제미나이 분석을 요청합니다.\n[{}]", userConcernAndCardResult);
        return geminiApiRequestConfiguration.getGeminiResponse(userConcernAndCardResult);
    }

    private String createGeminiRequestString(String concern, CardDTO card) {
        if (concern == null || card == null || card.getCardName() == null) {
            return "잘못된 입력입니다. 고민 내용과 카드 정보를 다시 확인해주세요.";
        }
        log.info(">>> 고민과 카드뽑기 내용을 조합한 summary 생성");
        return "현재 고민에 대한 유니버셜 타로카드 1카드 뽑기를 진행했어. \n"
                + "고민: " + concern + "\n"
                + "뽑기 결과: " + card.getCardName() + "카드가 "
                + (card.isReversed() ? "역방향" : "정방향")
                +"으로 나왔어. 한국말로 이 상황에서 이 카드가 무슨 의미일지 해석해줘.";
    }
}
