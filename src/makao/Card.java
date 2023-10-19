package makao;

public class Card {
    private Rank rank;
    private Suits suit;
    private boolean temp = false;

    public Card(Rank rank, Suits suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(Rank rank) {
        this.rank = rank;
    }

    public Card() {
    }


    public Rank getRank() {
        return rank;
    }

    public Suits getSuit() {
        return suit;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setSuit(Suits suit) {
        this.suit = suit;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        if(!rank.name().equals("JOKER")) {
            return rank + " " + suit + " " + Suits.giveSuitSymbol(suit);
        }
        else {
            return rank + "";
        }
    }
}
