import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecialDecisionComputer implements SpecialDecisionMaker {
    public Player player;

    public SpecialDecisionComputer(Player player) {
        this.player = player;
    }

    @Override
    public Card decide(Rank rank, Card stackCard) {
        String dana = "";

        switch (rank) {
            case J -> dana = Rank.specifyRankNumerically(chooseRankToDemand());
            case AS -> dana = chooseSuit();
            case JOKER -> dana = giveInformationAboutTheCard(stackCard);
        }

        Card card = createTempCard(dana, rank, stackCard);
        if (card.getRank() == null && card.getSuit() == null) {
            return decide(rank, stackCard);
        }
        return card;
    }


    private Rank chooseRankToDemand() {
        List<Card> nonFunctionalCards = new ArrayList<>();

        for (Card card : player.getCards()) {
            if (Rank.isCardNonFunctional(card.getRank())) {
                nonFunctionalCards.add(card);
            }
        }

        if (nonFunctionalCards.isEmpty()) {
            return Rank.chooseRandomNonFunctionalRank();
        }

        Collections.shuffle(nonFunctionalCards);
        return nonFunctionalCards.get(0).getRank();
    }

    private String chooseSuit() {
        return Suits.giveRandomSuit();
    }

    private String giveInformationAboutTheCard(Card stackCard) {
        String rankName = Rank.chooseRankForJoker();
        if (stackCard.getRank() == Rank.Q) {
            if (rankName.equals("K")) {
                return rankName + " " + Suits.giveAttackingKing();
            }
            else {
                return rankName + " " + Suits.giveRandomSuit();
            }
        }
        return rankName;
    }

}
