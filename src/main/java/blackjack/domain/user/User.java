package blackjack.domain.user;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.card.Deck;
import java.util.List;

public abstract class User {

    protected final Cards cards;
    protected final String name;
    protected int score = 0;

    protected User(String name) {
        cards = new Cards();
        this.name = name;
    }

    public void drawInitCards(Deck deck) {
        deck.drawInitCards()
                .stream()
                .forEach(cards::add);
    }

    public List<Card> showCards() {
        return cards.getCards();
    }

    public void drawCard(Deck deck) {
        cards.add(deck.drawCard());
    }

    public void calculate() {
        score = cards.getScore();
        validateNegative(score);
    }

    private void validateNegative(int score) {
        if (score < 0) {
            throw new RuntimeException("점수 계산에 문제가 있습니다.");
        }
    }

    public boolean isBust() {
        return cards.isBust(score);
    }

    public abstract boolean isDealer();

    public abstract List<Card> showInitCards();

    public abstract boolean isDrawable();

    public int getScore() {
        return score;
    }

    public String getName(){
        return name;
    }
}
