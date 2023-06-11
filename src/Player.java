
import java.util.List;
import java.util.Queue;

public class Player {
    private final int id;
    private final List<Card> cards;

    public Player(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public void giveCard(Card card){
        cards.add(card);
    }

    public void putTheCardOut(Card card){
        cards.remove(card);

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
