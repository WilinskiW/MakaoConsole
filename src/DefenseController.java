import java.util.ArrayList;
import java.util.List;

public class DefenseController {
    public List<Card> humanDefenseOption(Player human, Card stackCard) {
        System.out.println("||||| Defensywna Tura Gracza " + (human.getId() + 1) + " |||||");
        List<Card> defenseCards;

        if (stackCard.getRank().equals(Rank.FOUR)) {
            System.out.println("0. Czekaj");
            defenseCards = giveCardsAgainstWaiting(human);
            for (int i = 0; i < defenseCards.size(); i++) {
                System.out.println(i + 1 + ". " + defenseCards.get(0));
            }
        } else {
            System.out.println("0. Dobierz karty");
            defenseCards = giveCardAgainstPLUS(human, stackCard);
            for (int i = 0; i < defenseCards.size(); i++) {
                System.out.println(i + 1 + ". " + defenseCards.get(i));
            }
        }
        return defenseCards;
    }

    public List<Card> computerDefenseOption(Player computer, Card stackCard) {
        List<Card> defenseCards;
        if (stackCard.getRank().equals(Rank.FOUR)) {
            defenseCards = giveCardsAgainstWaiting(computer);
        } else {
            defenseCards = giveCardAgainstPLUS(computer, stackCard);
        }
        System.out.println(defenseCards);
        return defenseCards;
    }

    private List<Card> giveCardsAgainstWaiting(Player player) {
        List<Card> defenseCards = new ArrayList<>();
        for (Card card : player.getCards()) {
            if (card.getRank().equals(Rank.FOUR) || card.getRank().equals(Rank.JOKER)) {
                defenseCards.add(card);
            }
        }
        return defenseCards;
    }

    private List<Card> giveCardAgainstPLUS(Player player, Card stackCard) {
        List<Card> defenseCards = new ArrayList<>();
        for (Card card : player.getCards()) {
            if (isCardCanBeDefense(card, stackCard)) {
                defenseCards.add(card);
            }
        }
        return defenseCards;
    }

    private boolean isCardCanBeDefense(Card card, Card stackCard) {
        if (card.getRank() == stackCard.getRank()) {
            return true;
        } else if (card.getRank().equals(Rank.THREE) && card.getSuit() == stackCard.getSuit()) {
            return true;
        } else if (card.getRank().equals(Rank.JOKER)) {
            return true;
        } else return card.getRank().equals(Rank.TWO) && card.getSuit() == stackCard.getSuit();
    }

    public int findDefenseCardIndexInPlayerDeck(Card card, Player human) {
        for (int i = 0; i < human.getCards().size(); i++) {
            if (human.getCards().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }
}
