package blackjack.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.strategy.ShuffledDeckGenerateStrategy;
import blackjack.domain.user.User;
import blackjack.domain.user.Users;
import blackjack.domain.vo.BettingMoney;
import blackjack.domain.vo.Name;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlackJackTest {

    private static final int MINIMUM_BETTING_AMOUNT = 10;

    @DisplayName("플레이어 초기 카드 세팅 테스트")
    @Test
    public void setInitCardsPerPlayer() {
        //given
        Map<Name, BettingMoney> playerInfo = createPlayerInfo(List.of("pobi", "jason"));

        BlackJack blackJack = BlackJack.from(playerInfo, new ShuffledDeckGenerateStrategy());

        //when
        blackJack.setInitCardsPerPlayer();

        //then
        Users users = blackJack.getUsers();

        List<Integer> counts = users.getPlayers().stream()
                .map(user -> user.showCards().size())
                .collect(toList());

        User dealer = users.getDealer();

        int size = dealer.showCards().size();

        assertThat(counts.get(0)).isEqualTo(2);
        assertThat(counts.get(1)).isEqualTo(2);
        assertThat(size).isEqualTo(2);
    }

    @DisplayName("추가 카드를 뽑아가는 기능 테스트")
    @Test
    public void testDrawAdditionalCard() {
        //given
        Map<Name, BettingMoney> playerInfo = createPlayerInfo(List.of("pobi", "jason"));

        BlackJack blackJack = BlackJack.from(playerInfo, new ShuffledDeckGenerateStrategy());

        //when
        blackJack.drawAdditionalCard(player -> blackJack.drawCardFromUser(player), dealer -> blackJack.drawCardFromUser(dealer));

        //then
        Users users = blackJack.getUsers();

        List<Integer> playersSize = users.getPlayers().stream()
                .map(user -> user.showCards().size())
                .collect(toList());

        int dealerSize = users.getDealer().showCards().size();

        assertThat(playersSize.get(0)).isEqualTo(1);
        assertThat(playersSize.get(1)).isEqualTo(1);
        assertThat(dealerSize).isEqualTo(1);
    }

    private Map<Name, BettingMoney> createPlayerInfo(List<String> names) {
        Map<Name, BettingMoney> playerInfo = new HashMap<>();

        names.stream()
                .forEach(name -> playerInfo.put(Name.of(name), new BettingMoney(MINIMUM_BETTING_AMOUNT)));

        return playerInfo;
    }
}