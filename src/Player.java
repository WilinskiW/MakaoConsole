
import java.util.List;

public class Player {
    private final int id;
    private final List<Card> cards;
    private boolean isWinner = false;
    private boolean skipTurnActive = false;

    public Player(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public void giveCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void setSkipTurnActive(boolean skipTurnActive) {
        this.skipTurnActive = skipTurnActive;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public int getId() {
        return id;
    }

    public boolean isSkipTurnActive() {
        return skipTurnActive;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", cards=" + cards +
                '}';
    }
}
