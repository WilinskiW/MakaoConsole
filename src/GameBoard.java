import java.util.*;

public class GameBoard {
    private final Queue<Card> boardDeck;
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
            players.add(new Player(i + 1, new ArrayList<>()));
        }
        this.players = players;
    }

    public void givePlayersStartingCards(){
        for(Player player : players){
            for(int i = 0; i < 5; i++){
                player.giveCard(boardDeck.poll());
            }
        }
    }

    public void putStartingCardOnStack(){
        stack.add(boardDeck.poll());
    }

    public void addCardToStack(Card card){
        stack.add(card);
    }

    public boolean compareCards(Card card1, Card card2){
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public void putCardOnStack(Card card, Player user){
        addCardToStack(card);
        user.removeCard(card);
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
