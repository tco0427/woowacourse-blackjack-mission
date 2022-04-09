package blackjack.domain.user;

import blackjack.domain.card.Card;
import java.util.List;

public class Player extends User {

    private static final int DRAW_THRESHOLD = 21;
    private static final int INIT_COUNT = 2;

    private final BettingMoney money;

    private Player(String name, BettingMoney money) {
        super(name);
        this.money = money;
    }

    @Override
    public boolean isDealer() {
        return false;
    }

    public static Player from(String name, BettingMoney money) {
        validateName(name);

        return new Player(name, money);
    }

    private static void validateName(String playerName) {
        validateEmpty(playerName);
        validateSize(playerName);
    }

    private static void validateSize(String playerName) {
        if (playerName.length() == 0) {
            throw new IllegalArgumentException("참여자 이름이 공백일 수 없습니다.");
        }
    }

    private static void validateEmpty(String playerName) {
        if (playerName.contains(" ")) {
            throw new IllegalArgumentException("이름에 공백을 포함할 수 없습니다.");
        }
    }

    @Override
    public List<Card> showInitCards() {
        return cards.getLimitedCard(INIT_COUNT);
    }

    @Override
    public boolean isDrawable() {
        int sumPoint = cards.getSumPoint();

        return sumPoint < DRAW_THRESHOLD;
    }
}
