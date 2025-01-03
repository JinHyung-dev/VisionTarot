package com.visiontarot.card.service;

import com.visiontarot.card.model.Card;
import com.visiontarot.card.model.CardDTO;
import com.visiontarot.card.repository.CardRepository;
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
        this.cardDTOS = getAllCards();
    }

    public CardDTO drawOneCard() {
        log.info(">>> 1카드 뽑기 실행 <<<");
        Random random = new Random();
        int index = random.nextInt(cardDTOS.size());
        CardDTO card = cardDTOS.get(index);
        log.info("결과 : " + card.toString());
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
}
