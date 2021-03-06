package blackjack.domain;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import blackjack.domain.card.Deck;
import blackjack.domain.strategy.DeckGenerateStrategy;
import blackjack.domain.user.Dealer;
import blackjack.domain.user.Player;
import blackjack.domain.user.User;
import blackjack.domain.user.Users;
import blackjack.domain.vo.BettingMoney;
import blackjack.domain.vo.Name;
import blackjack.dto.UserDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlackJack {

    private final Deck deck;
    private final Users users;

    private BlackJack(Deck deck, Users users) {
        this.deck = deck;
        this.users = users;
    }

    public static BlackJack from(Map<Name, BettingMoney> playerInfo, DeckGenerateStrategy deckGenerateStrategy) {
        Users users = createUser(playerInfo);

        Deck deck = new Deck(deckGenerateStrategy);

        return new BlackJack(deck, users);
    }

    private static Users createUser(Map<Name, BettingMoney> playerInfo) {
        List<Player> players = playerInfo.entrySet()
                .stream()
                .map(entry -> Player.from(entry.getKey(), entry.getValue()))
                .collect(toList());

        Dealer dealer = Dealer.create();

        return Users.of(players, dealer);
    }

    public void setInitCardsPerPlayer() {
        users.drawCards(deck);
        users.drawCards(deck);
    }

    public void drawCardFromUser(User user) {
        user.drawCard(deck);
    }

    public void drawAdditionalCard(Consumer<User> consumerPlayer, Consumer<User> consumerDealer) {
        users.drawPlayerAdditionalCard(consumerPlayer);
        users.drawDealerAdditionalCard(consumerDealer);
    }

    public Map<UserDto, Integer> getResultCardInfo() {
        Map<UserDto, Integer> result = new LinkedHashMap<>();

        User dealer = users.getDealer();
        result.put(UserDto.from(dealer), dealer.getScore());

        Map<UserDto, Integer> playerResult = users.getPlayers()
                .stream()
                .collect(toMap(UserDto::from, User::getScore,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));

        result.putAll(playerResult);

        return result;
    }

    public Map<String, Integer> calculateRevenueAllUser() {
        List<Player> players = users.getPlayers();
        Dealer dealer = users.getDealer();

        return Result.calculateRevenue(players, dealer);
    }

    public Users getUsers() {
        return this.users;
    }
}
