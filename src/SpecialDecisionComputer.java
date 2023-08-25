import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpecialDecisionComputer implements SpecialDecisionMaker{
public Player player;

    public SpecialDecisionComputer(Player player) {
        this.player = player;
    }
    @Override
    public Card decide(Rank rank) {
        String dana = "";

        switch (rank){
            case J -> dana = Rank.specifyRankNumerically(chooseRankToDemand());
            case AS -> dana = chooseSuit();
        }

        Card card = createTempCard(dana);
        if(card.getRank() == null && card.getSuit() == null){
            return decide(rank);
        }
        return card;
    }



    private Rank chooseRankToDemand(){
        List<Card> nonFunctionalCards = new ArrayList<>();

        for(Card card : player.getCards()){
            if(Rank.isCardNonFunctional(card.getRank())){
                nonFunctionalCards.add(card);
            }
        }

        if(nonFunctionalCards.isEmpty()){
            return Rank.chooseRandomNonFunctionalRank();
        }

        Collections.shuffle(nonFunctionalCards);
        return nonFunctionalCards.get(0).getRank();
    }

    private String chooseSuit(){
        return Suits.giveRandomSuit();
    }
}
