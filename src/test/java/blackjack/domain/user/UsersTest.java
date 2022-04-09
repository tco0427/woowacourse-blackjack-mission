package blackjack.domain.user;

import static blackjack.Fixtures.DIAMOND_TEN;
import static blackjack.Fixtures.SPADE_ACE;
import static blackjack.TestUtils.createPlayerByName;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Deck;
import blackjack.domain.strategy.ShuffledDeckGenerateStrategy;
import blackjack.domain.vo.BettingMoney;
import blackjack.domain.vo.Name;
import java.util.ArrayDeque;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersTest {

    @DisplayName("문자열 기반 Users 생성 검증")
    @Test
    public void testCreateUsers() {
        //given
        List<String> names = List.of("pobi", "jason");

        List<Player> players = names.stream()
                .map(name -> createPlayerByName(name))
                .collect(toList());

        //when
        Users users = Users.of(players, Dealer.create());

        //then
        Assertions.assertAll(
                () -> assertThat(users).isNotNull(),
                () -> assertThat(users.getPlayers().size()).isEqualTo(2)
        );
    }

    @DisplayName("추가 카드 뽀는 로직 검증")
    @Test
    public void testDrawAdditionalCard() {
        //given
        List<String> names = List.of("pobi", "jason");

        List<Player> players = names.stream()
                .map(name -> createPlayerByName(name))
                .collect(toList());

        Users users = Users.of(players, Dealer.create());

        Deck deck = new Deck(new ShuffledDeckGenerateStrategy());

        //when
        users.drawPlayerAdditionalCard(user -> user.drawCard(deck));
        users.drawDealerAdditionalCard(dealer -> dealer.drawCard(deck));

        //then
        List<Integer> count = users.getPlayers()
                .stream()
                .map(user -> user.showCards().size())
                .collect(toList());

        int size = users.getDealer().showCards().size();

        assertThat(count.get(0)).isEqualTo(1);
        assertThat(count.get(1)).isEqualTo(1);
        assertThat(size).isEqualTo(1);
    }

    @DisplayName("블랙잭(처음 2장이 21)인지 판단하는 기능 테스트")
    @Test
    public void testBlackJack() {
        //given
        Name name = Name.of("pobi");

        User player = Player.from(name, new BettingMoney(10000));

        Deck deck = new Deck(() -> new ArrayDeque<>(List.of(SPADE_ACE, DIAMOND_TEN)));

        //when
        player.drawCard(deck);
        player.drawCard(deck);

        //then
        assertThat(player.isBlackJack()).isTrue();

    }
}