package com.visiontarot.service;

import com.visiontarot.domain.Card;
import com.visiontarot.dto.CardDTO;
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

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
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
}
