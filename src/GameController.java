import java.util.*;

public class GameController {
    private final GameBoard gameBoard = new GameBoard();

    public void start() {
        System.out.println("Zaczyna się gra");
        int amountOfPlayers = decideHowManyPlayers();
        gameBoard.preparPlayers(amountOfPlayers);
        System.out.println("Przygotowano " + amountOfPlayers + " graczy");
        System.out.println("Rozdano każdemu po 5 kart");
        gameBoard.givePlayersStartingCards();
        System.out.println("Położono pierwszą kartą na stos");
        gameBoard.putStartingCardOnStack();
        System.out.println(gameBoard);
        System.out.println("Rozegraj turę");
        playTurn(amountOfPlayers);
        System.out.println(gameBoard);
    }

    private int decideHowManyPlayers() {
        Scanner scanner = new Scanner(System.in);
        int amountOfPlayers;

        do {
            System.out.println("Podaj ilość graczy (min.2, max.5)");
            amountOfPlayers = scanner.nextInt();
        }
        while (amountOfPlayers < 2 || amountOfPlayers > 5);

        return amountOfPlayers;
    }

    private void playTurn(int players) {
        do {
            boolean humanWon = humanTurn();
            if (humanWon) {
                continue;
            }
            computerTurns(players);
        } while (!isVictoryAchieve());
    }

    private boolean humanTurn() {
        Player human = gameBoard.getPlayers().get(0);

        int amountOfCards = human.getCards().size();
        if (human.isSkipTurnActive()) {
            executeSkipTurnSpecial(human);
            return human.isWinner();
        }
        showTurnOptions(human);
        boolean turnEnded;
        do {
            int playerChoice = checkHumanChoice(amountOfCards);
            turnEnded = executeTurn(playerChoice, human);
        }
        while (!turnEnded);
        endTurnUpdate(human);

        return human.isWinner();
    }

    private void executeSkipTurnSpecial(Player player) {
        System.out.println("///// Tura Gracza " + (player.getId() + 1) + " /////");
        System.out.println("Gracz " + (player.getId() + 1) + " czeka");
        player.setSkipTurnActive(false);
        gameBoard.checkBoardDeckStatus();
    }

    private void showTurnOptions(Player player) {
        System.out.println("///// Tura Gracza " + (player.getId() + 1) + " /////");
        System.out.println("Karta na stosie: " + gameBoard.getStack().getLast());
        System.out.println("0. Dobierz kartę");
        for (int i = 0; i < player.getCards().size(); i++) {
            System.out.println(i + 1 + ". " + player.getCards().get(i));
        }
    }

    private int checkHumanChoice(int amountOfCards) {
        Scanner scanner = new Scanner(System.in);
        int humanChoice;
        do {
            System.out.println("Podaj liczbę od 0 do " + amountOfCards);
            humanChoice = scanner.nextInt();
        }
        while (humanChoice < 0 || humanChoice > amountOfCards);
        return humanChoice;
    }

    private boolean executeTurn(int playerChoice, Player player) { //boolean - czy tura zakończona
        if (playerChoice == 0) { //dobiera
            player.giveCard(gameBoard.getBoardDeck().poll());
            System.out.println("Gracz " + (player.getId() + 1) + " dobiera");
            return true;
        }
        //wybrana karta
        if (isCorrectCard(playerChoice, player.getCards(), player)) {
            Card chosenCard = player.getCards().get(playerChoice - 1);
            gameBoard.putCardOnStack(chosenCard, player);
            gameBoard.useCardAbility(chosenCard, player.getId());
            System.out.println("Gracz " + (player.getId() + 1) + " wykłada " + chosenCard);
            return true;
        } else {
            //System.out.println("Nie możesz położyć tej karty! Kolor kart lub stopień musi się zgadzać! Jeżeli nie możesz wyłożyć karty, dobierz kartę! ");
        }
        return false;
    }

    private void endTurnUpdate(Player player) {
        gameBoard.setVictoryStatus(player);
        gameBoard.checkBoardDeckStatus();
    }

    private void computerTurns(int players) {
        for (int id = 2; id <= players; id++) { //tury komputera
            if (computerTurn(id)) {
                break;
            }
        }
    }

    private boolean computerTurn(int id) {
        Player computer = gameBoard.getPlayers().get(id - 1);
        int amountOfCards = computer.getCards().size();
        Card stackCard = gameBoard.getStack().getLast();

        if (computer.isSkipTurnActive()) {
            executeSkipTurnSpecial(computer);
            return computer.isWinner();
        }
        showComputerInformation(stackCard, computer, amountOfCards);

        List<Card> validCards = findValidCards(amountOfCards, computer, stackCard);
        boolean turnEnded;

        do {
            int playerChoice = checkComputerChoice(validCards, computer);
            turnEnded = executeTurn(playerChoice, computer);
        }
        while (!turnEnded);

        endTurnUpdate(computer);

        return computer.isWinner();
    }

    //generowanie valid kart
    //numer <- player wybiera kartę lub dobieranie
    //numer ->  karta jest zagrana lub dobrana
    //powtarzanie wyboru jeśli niepoprany


    private void showComputerInformation(Card stackCard, Player computer, int amountOfCards) {
        System.out.println("///// Tura Gracza " + (computer.getId() + 1) + " /////");
        System.out.println("Karta na wierzchu stosu: " + stackCard);
        System.out.println("Gracz " + (computer.getId() + 1) + " ma " + amountOfCards + " kart");
    }

    private List<Card> findValidCards(int amountOfCards, Player player, Card stackCard) {
        List<Card> validCards = new ArrayList<>();
        for (int i = 0; i < amountOfCards; i++) {
            Card checkedCard = player.getCards().get(i);
            if (gameBoard.compareCards(checkedCard, stackCard)) {
                validCards.add(checkedCard);
            }
        }
        return validCards;
    }

    private int checkComputerChoice(List<Card> validCards, Player computer) {
        if (validCards.isEmpty()) {
            return 0;
        }

        return new Random().nextInt(computer.getCards().size()) + 1;
    }


    private boolean isCorrectCard(int choice, List<Card> playerCards, Player player) {
        Card stackCard = gameBoard.getStack().getLast();

        if (stackCard.getRank().name().equals("Q")) {
            return true;
        }

        if (choice == 0 || choice > playerCards.size()) {
            return false;
        }
        Card chosenCard = player.getCards().get(choice - 1);

        return gameBoard.compareCards(chosenCard, stackCard);
    }
//todo dodać warunek do Jokera i samego Jokera


    private boolean isVictoryAchieve() {
        for (Player player : gameBoard.getPlayers()) {
            if (player.isWinner()) {
                System.out.println("Wygrywa Gracz " + (player.getId() + 1));
                return true;
            }
        }
        return false;
    }


}