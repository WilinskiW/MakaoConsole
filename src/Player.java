
import java.util.List;

public class Player {
    private final int id;
    private final List<Card> cards;
    private boolean isWinner = false;

    public Player(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public void giveCard(Card card){
        cards.add(card);
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public int getId() {
        return id;
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
