public class Card {
    private final Rank rank;
    private final Suits suit;

    public Card(Rank rank, Suits suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " " + suit;
    }
}
