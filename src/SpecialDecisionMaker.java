import java.nio.charset.CoderResult;
import java.util.Arrays;

public interface SpecialDecisionMaker {
    Card decide(Rank rank, Card stackCard);

    default Card createTempCard(String dana, Rank chosenCardRank, Card stackCard) {
        Card card = new Card();

        if (chosenCardRank.name().equals("JOKER")) {
           return createJokerCard(dana,stackCard);
        }

        if (Character.isDigit(dana.charAt(0))) { //J
            Rank rank = Rank.specifyRankInWords(Integer.parseInt(dana));
            card.setRank(rank);
        } else {
            Suits suit = Suits.giveSuit(dana); //AS
            card.setSuit(suit);
        }
        return card;
    }

    private Card createJokerCard(String dana, Card stackCard) {
        Card card = new Card();
        String[] splitDana = dana.split(" ", 2);

        if (splitDana.length == 1) {
            if (Rank.specifyRankInWords(dana) != null) {
                Rank rank = Rank.specifyRankInWords(dana);
                card.setRank(rank);
                card.setSuit(stackCard.getSuit());
            } else {
                Suits suit = Suits.giveSuit(dana);
                card.setSuit(suit);
                card.setRank(stackCard.getRank());
            }
            return card;
        }

        if(stackCard.getRank() == Rank.Q){
            Rank rank = Rank.specifyRankInWords(splitDana[0]);
            card.setRank(rank);
            Suits suit = Suits.giveSuit(splitDana[1]);
            card.setSuit(suit);
        }

        return card;
    }
}
