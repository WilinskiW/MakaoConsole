import java.util.*;

public class GameBoard {
    private Queue<Card> boardDeck;
    private List<Player> players;
    private final Deque<Card> stack = new LinkedList<>();

    public GameBoard() {
        boardDeck = createBoardDeck();
    }


    private Queue<Card> createBoardDeck() {
        LinkedList<Card> deck = new LinkedList<>();

        for (Rank rank : Rank.values()) {
            for (Suits suit : Suits.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }


    public void preparPlayers(int count) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            players.add(new Player(i, new ArrayList<>()));
        }
        this.players = players;
    }

    public void givePlayersStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                player.giveCard(boardDeck.poll());
            }
        }
    }

    public void putStartingCardOnStack() {
        stack.add(boardDeck.poll());
    }

    public void addCardToStack(Card card) {
        stack.add(card);
    }

    public boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public void putCardOnStack(Card card, Player user) {
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

        for (int cardIndex = 0; cardIndex < stack.size() - 1; cardIndex++) {
            newDeck.add(stack.getFirst());
        }

        this.boardDeck = newDeck;
    }

    public void checkBoardDeckStatus() {
        if (boardDeck.size() == 1) {
            refreshBoardDeck();
        }
    }

    public void useCardAbility(Card chosenCard, int currentPlayerId) {
        String cardNameRank = chosenCard.getRank().name();

        switch (cardNameRank) {
            //case "AS" -> System.out.println("Change suit");
            case "TWO" -> useTwoAbility(currentPlayerId);
            case "THREE" -> useThreeAbility(currentPlayerId);
            case "FOUR" -> useFourAbility(currentPlayerId);
            //case "J" -> System.out.println("Demand");
            case "K" -> useKingAbility(currentPlayerId, chosenCard);
        }
    }

    private void useTwoAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (currentPlayerId != lastIndex) {
            giveNextPlayerCards(currentPlayerId + 1,2);
        } else {
            giveNextPlayerCards(0,2);
        }
    }

    private void giveNextPlayerCards(int nextPlayerIndex, int amountOfCards) {
        for (int i = 0; i < amountOfCards; i++) {
            players.get(nextPlayerIndex).giveCard(boardDeck.poll());
        }
    }

    private void useThreeAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (currentPlayerId != lastIndex) {
            giveNextPlayerCards(currentPlayerId + 1,3);
        } else {
            giveNextPlayerCards(0,3);
        }

    }

    private void useFourAbility(int currentPlayerId) {
        int lastIndex = players.size() - 1;
        if (lastIndex != currentPlayerId) {
            players.get(currentPlayerId + 1).setSkipTurnActive(true);
        } else {
            players.get(0).setSkipTurnActive(true);
        }
    }

    private void useKingAbility(int currentPlayerId, Card chosenCard){
        int lastIndex = players.size() - 1;
        String cardSuits = chosenCard.getSuit().name();

        switch (cardSuits){
            case "KIER" -> useHeartKing(currentPlayerId, lastIndex);
            case "PIK" -> useSpadeKing(currentPlayerId, lastIndex);
        }

    }

    private void useHeartKing(int currentPlayerId, int lastIndex){
        if (currentPlayerId != lastIndex) {
            giveNextPlayerCards(currentPlayerId + 1,5);
        } else {
            giveNextPlayerCards(0,5);
        }
    }

    private void useSpadeKing(int currentPlayerId, int lastIndex){
        if (currentPlayerId != 0) {
            giveNextPlayerCards(currentPlayerId - 1,5);
        } else {
            giveNextPlayerCards(lastIndex,5);
        }
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


    @Override
    public String toString() {
        return "GameBoard{" +
                "boardDeck=" + boardDeck + " " + boardDeck.size() +
                ", players=" + players +
                ", stack=" + stack +
                '}';
    }


}
