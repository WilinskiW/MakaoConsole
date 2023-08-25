
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final int id;
    private final List<Card> cards = new ArrayList<>();
    private boolean isWinner = false;
    private boolean skipTurnActive = false;
    private boolean demanding = false;
    private boolean demanded = false;

    public Player(int id) {
        this.id = id;
    }

    public abstract SpecialDecisionMaker getDecisionMaker();

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

    public void setDemanding(boolean demanding) {
        this.demanding = demanding;
    }

    public void setDemanded(boolean demanded) {
        this.demanded = demanded;
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

    public boolean isDemanding() {
        return demanding;
    }

    public boolean isDemanded() {
        return demanded;
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
