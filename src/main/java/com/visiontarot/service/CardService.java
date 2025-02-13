package com.visiontarot.service;

import com.visiontarot.config.GeminiApiRequestConfiguration;
import com.visiontarot.domain.Card;
import com.visiontarot.domain.CardDTO;
import com.visiontarot.repository.CardRepository;
import java.util.List;
import java.util.Map;
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
        if (cardDTOS == null || cardDTOS.isEmpty()) { // 카드 정보가 없다면 조회
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
        return cards.stream()
                .map(Card::toDTO)  // 엔티티 -> DTO로 변환
                .collect(Collectors.toList());
    }

    public Map<String, Object> getGeminiResponse(String userConcernAndCardResult) {
        return geminiApiRequestConfiguration.makeRequest(userConcernAndCardResult);
    }
}
