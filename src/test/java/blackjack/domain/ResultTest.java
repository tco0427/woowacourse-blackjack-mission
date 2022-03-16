package blackjack.domain;

import static blackjack.domain.Result.LOSS;
import static blackjack.domain.Result.WIN;
import static blackjack.domain.card.Denomination.ACE;
import static blackjack.domain.card.Denomination.FIVE;
import static blackjack.domain.card.Denomination.KING;
import static blackjack.domain.card.Denomination.QUEEN;
import static blackjack.domain.card.Denomination.SEVEN;
import static blackjack.domain.card.Denomination.SIX;
import static blackjack.domain.card.Denomination.TWO;
import static blackjack.domain.card.Suit.CLOVER;
import static blackjack.domain.card.Suit.DIAMOND;
import static blackjack.domain.card.Suit.SPADE;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.user.BettingMoney;
import blackjack.domain.user.Dealer;
import blackjack.domain.user.Player;
import blackjack.domain.vo.Name;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResultTest {

    private static final int MINIMUM_BETTING_AMOUNT = 10;

    @DisplayName("게임 결과를 제대로 반환하는지 테스트")
    @Test
    public void testGetResult() {
        //given
        Deck deck = initDeck();

        List<Player> players = List.of(
                createPlayerByName("pobi"),
                createPlayerByName("jason")
        );

        for (Player player : players) {
            player.drawCard(deck);
            player.drawCard(deck);
        }

        Dealer dealer = Dealer.create();
        dealer.drawCard(deck);
        dealer.drawCard(deck);

        //when
        Map<String, Integer> result = Result.calculateRevenue(players, dealer);

        //then
        assertThat(result.get(players.get(0).getName())).isEqualTo((int)(MINIMUM_BETTING_AMOUNT * LOSS.getRate()));
        assertThat(result.get(players.get(1).getName())).isEqualTo((int)(MINIMUM_BETTING_AMOUNT * WIN.getRate()));
    }

    @DisplayName("둘 다 버스트가 아니고 딜러가 작은 경우")
    @Test
    public void testWinPlayerWithNotBust() {
        //given
        Deck deck = new Deck(() -> new ArrayDeque<>(List.of(
                new Card(SPADE,ACE),
                new Card(SPADE,SEVEN),
                new Card(SPADE, TWO),
                new Card(SPADE, QUEEN)
        )));

        List<Player> players = List.of(createPlayerByName("pobi"));

        for (Player player : players) {
            player.drawCard(deck);
            player.drawCard(deck);
        }

        Dealer dealer = Dealer.create();
        dealer.drawCard(deck);
        dealer.drawCard(deck);

        //when
        Map<String, Integer> result = Result.calculateRevenue(players, dealer);

        //then
        assertThat(result.get(players.get(0).getName())).isEqualTo(MINIMUM_BETTING_AMOUNT);
    }

    private Deck initDeck() {
        return new Deck(() -> new ArrayDeque<>(List.of(
                new Card(CLOVER, ACE),
                new Card(DIAMOND, FIVE),    //16
                new Card(SPADE, KING),
                new Card(SPADE, ACE),   //21
                new Card(DIAMOND, SIX),
                new Card(SPADE, ACE)    //17
        )));
    }

    private Player createPlayerByName(String name) {
        return Player.from(Name.of(name), new BettingMoney(MINIMUM_BETTING_AMOUNT));
    }
}