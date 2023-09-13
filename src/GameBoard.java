import java.util.*;

public class GameBoard {
    private Queue<Card> boardDeck;
    private List<Player> players;
    private final Deque<Card> stack = new LinkedList<>();
    private final Deque<Card> pullDeck = new LinkedList<>();

    public GameBoard() {
        boardDeck = createBoardDeck();
    }


    private Queue<Card> createBoardDeck() {
        LinkedList<Card> deck = new LinkedList<>();


        for (Rank rank : Rank.values()) {
            if (rank.equals(Rank.JOKER)) {
                continue;
            }
            for (Suits suit : Suits.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        deck.add(new Card(Rank.JOKER));
        deck.add(new Card(Rank.JOKER));

        Collections.shuffle(deck);
        return deck;
    }


    public void preparPlayers(int count) {
        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(0));
        for (int i = 1; i < count; i++) {
            players.add(new ComputerPlayer(i));
        }
        this.players = players;
    }

    public void givePlayersStartingCards() {
        players.get(1).giveCard(new Card(Rank.JOKER));
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                player.giveCard(boardDeck.poll());
            }
        }
    }

    public void putStartingCardOnStack() {
        for (Card card : boardDeck) {
            if (Rank.isCardNonFunctional(card.getRank())) {
                addCardToStack(card);
            }
        }
    }

    public void addCardToStack(Card card) {
        stack.add(card);
    }

    public boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public void addCardToStack(Card card, Player user) {
        addCardToStack(card);
        user.removeCard(card);
    }

    public void setVictoryStatus(Player player) {
        if (player.getCards().size() == 0) {
            player.setWinner(true);
        }
    }

    private void refreshBoardDeck() {
        LinkedList<Card> newDeck = new LinkedList<>();

        int startingSize = stack.size() - 1;

        for (int cardIndex = 0; cardIndex < startingSize; cardIndex++) {
            Card card = stack.pollFirst();
            System.out.println(card);
            if (!card.isTemp()) {
                newDeck.add(card);
            }
        }
        this.boardDeck = newDeck;
        System.out.println(boardDeck);
    }

    public void checkBoardDeckStatus() {
        if (boardDeck.size() < 1) {
            refreshBoardDeck();
        }
    }

    public void useCardAbility(Card chosenCard, int currentPlayerId, Card decision) {
        String cardNameRank = chosenCard.getRank().name();

        switch (cardNameRank) {
            case "AS" -> useAceAbility(decision);
            case "TWO" -> useTwoAbility(currentPlayerId);
            case "THREE" -> useThreeAbility(currentPlayerId);
            case "FOUR" -> useFourAbility(currentPlayerId);
            case "J" -> useJackAbility(currentPlayerId, decision);
            case "K" -> useKingAbility(currentPlayerId, chosenCard);
            case "JOKER" -> useJokerAbility(currentPlayerId, decision);
        }
    }

    private void useAceAbility(Card decision) {
        Card card = new Card(Rank.AS, decision.getSuit());
        card.setTemp(true);
        addCardToStack(card);
    }

    private void useTwoAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (currentPlayerId != lastIndex) {
            addCardsToPullDeck(2);
            setAttackingStatusToNextPlayer(currentPlayerId + 1);
        } else {
            addCardsToPullDeck(2);
            setAttackingStatusToNextPlayer(0);
        }
    }

    private void addCardsToPullDeck(int amountOfCards) {
        for (int i = 0; i < amountOfCards; i++) {
            pullDeck.add(boardDeck.poll());
        }
    }

    private void setAttackingStatusToNextPlayer(int nextPlayerIndex) {
        players.get(nextPlayerIndex).setAttacked(true);
    }

    private void useThreeAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (currentPlayerId != lastIndex) {
            addCardsToPullDeck(3);
            setAttackingStatusToNextPlayer(currentPlayerId + 1);
        } else {
            addCardsToPullDeck(3);
            setAttackingStatusToNextPlayer(0);
        }

    }

    private void useFourAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (lastIndex != currentPlayerId) {
            players.get(currentPlayerId + 1).setSkipTurnActive(true);
            setAttackingStatusToNextPlayer(currentPlayerId + 1);
        } else {
            players.get(0).setSkipTurnActive(true);
            setAttackingStatusToNextPlayer(0);
        }
    }

    private void useJackAbility(int currentPlayerId, Card decision) {
        Card card = new Card(decision.getRank(), Suits.giveSuit(Suits.giveRandomSuit()));
        card.setTemp(true);
        addCardToStack(card);

        players.get(currentPlayerId).setDemanding(true);
        for (Player player : players) {
            if (player != players.get(currentPlayerId)) {
                player.setDemanded(true);
            }
        }
    }


    private void useKingAbility(int currentPlayerId, Card chosenCard) {
        int lastIndex = players.size() - 1;
        String cardSuits = chosenCard.getSuit().name();

        switch (cardSuits) {
            case "KIER" -> useHeartKing(currentPlayerId, lastIndex);
            case "PIK" -> useSpadeKing(currentPlayerId, lastIndex);
        }

    }

    private void useHeartKing(int currentPlayerId, int lastIndex) {
        if (currentPlayerId != lastIndex) {
            addCardsToPullDeck(5);
            setAttackingStatusToNextPlayer(currentPlayerId + 1);
        } else {
            addCardsToPullDeck(5);
            setAttackingStatusToNextPlayer(0);
        }
    }

    private void useSpadeKing(int currentPlayerId, int lastIndex) {
        if (currentPlayerId != 0) {
            addCardsToPullDeck(5);
            setAttackingStatusToNextPlayer(currentPlayerId - 1);
        } else {
            addCardsToPullDeck(5);
            setAttackingStatusToNextPlayer(lastIndex);
        }
    }

    private void useJokerAbility(int currentPlayerId, Card decision) {
        Card card = new Card(decision.getRank(), decision.getSuit());
        card.setTemp(true);
        addCardToStack(card);

        if (!decision.getRank().equals(Rank.J)) {
            useCardAbility(card, currentPlayerId, decision);
        } else {
            Card cardJ = players.get(currentPlayerId).getDecisionMaker().decide(decision.getRank(), stack.getLast());
            useCardAbility(card, currentPlayerId, cardJ);
        }
    }

    public void givePlayerPullDeck(Player player) {
        for (Card card : pullDeck) {
            player.giveCard(card);
        }
        pullDeck.clear();
    }

    public Queue<Card> getBoardDeck() {
        return boardDeck;
    }

    public Deque<Card> getStack() {
        return stack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deque<Card> getPullDeck() {
        return pullDeck;
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                "boardDeck=" + boardDeck + " " + boardDeck.size() +
                ", players=" + players +
                ", stack=" + stack +
                ", pullDeck=" + pullDeck +
                '}';
    }


}
