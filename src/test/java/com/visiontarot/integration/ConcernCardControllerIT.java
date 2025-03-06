package com.visiontarot.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.visiontarot.dto.CardResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcernCardControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 고민카드생성_제미나이응답_리턴CardResponseDTO() {
        String prevGeminiAnswer = "Queen of Cups 카드가 정방향으로 나왔다는 것은, 당신의 개발자로서의 성공 가능성에 대해 긍정적인 메시지를 전달하고 있습니다. 단순히 성공할 수 있다는 긍정적인 답변 이상의 의미를 담고 있죠."
                + "이 카드는 감정적 지능, 직관, 공감 능력과 같은 내면의 힘을 상징합니다. 개발은 단순히 기술적인 능력만으로 이루어지는 것이 아니기 때문에, Queen of Cups는 당신이 개발자로서 성공할 수 있는 중요한 자질을 갖추고 있음을 보여줍니다."
                + "구체적으로는 다음과 같은 의미로 해석할 수 있습니다.\n"
                + " 강한 감정적 지능: 당신은 사용자의 니즈를 이해하고, 그들의 요구사항을 만족시키는 개발을 할 수 있는 능력을 가지고 있습니다. 단순히 기능만 구현하는 것이 아니라, 사용자 경험(UX)에 대한 깊은 이해를 바탕으로 감동을 주는 제품을 만들 수 있다는 것을 의미합니다."
                + " 뛰어난 직관: 문제 해결 능력이 뛰어나고, 어려운 문제에 직면했을 때 직관적으로 최적의 해결책을 찾아낼 수 있습니다. 코딩 과정에서 발생하는 예상치 못한 문제들에도 효과적으로 대처할 수 있다는 것을 의미합니다."
                + " 내면의 평정심: 개발 과정은 압박감과 스트레스가 많은 경우가 많습니다. Queen of Cups는 당신이 이러한 어려움 속에서도 내면의 평정심을 유지하고 꾸준히 노력할 수 있는 능력을 가지고 있음을 시사합니다."
                + " 자기 관리 능력: 당신은 자신을 잘 돌보고, 자신의 감정과 한계를 잘 이해하며, 건강하게 개발 생활을 이어갈 수 있음을 의미합니다. 번아웃을 방지하고 장기적으로 성공적인 개발자의 삶을 유지할 수 있는 능력을 나타냅니다."
                + "하지만, Queen of Cups는 단순히 타고난 재능만을 의미하는 것은 아닙니다. 당신의 노력과 꾸준한 자기계발이 뒷받침되어야 성공으로 이어질 수 있다는 점을 명심해야 합니다. 이 카드는 당신에게 내면의 강점을 활용하고, 꾸준히 노력한다면 개발자로서 성공할 가능성이 매우 높다는 긍정적인 메시지를 전달하고 있습니다.";

        ResponseEntity<CardResponseDTO> response = restTemplate.postForEntity(
                "/concern-card/create",
                prevGeminiAnswer,
                CardResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getGeminiAnswer()).isNotEmpty();
        assertThat(response.getBody().getConcernCardImageUrl()).isNotEmpty();
    }
}
