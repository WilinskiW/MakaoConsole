import java.util.Scanner;

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
        playTurn();
        System.out.println(gameBoard);
    }

    private void playTurn() {
        playerTurn();
        computerTurn();
    }

    private void playerTurn() {
        Player player = gameBoard.getPlayers().get(0);
        int amountOfCards = player.getCards().size();

        System.out.println("//////Tura gracza//////");

        System.out.println("0. Dobierz kartę");
        for (int i = 0; i < player.getCards().size(); i++) {
            System.out.println(i + 1 + ". " + player.getCards().get(i));
        }

        //Akcje gracza:

        Scanner scanner = new Scanner(System.in);
        int playerChoice;
        do {
            System.out.println("Podaj liczbę od 0 do " + amountOfCards);
            playerChoice = scanner.nextInt();
        }
        while (!((playerChoice >= 0 && playerChoice <= amountOfCards) && isChoiceCorrect(playerChoice)));

        if (playerChoice == 0) {
            player.giveCard(gameBoard.getBoardDeck().poll());
        } else {
            Card removedCard = player.getCards().get(playerChoice - 1);
            gameBoard.getStack().add(removedCard);
            player.putTheCardOut(removedCard);
        }


    }


    private void computerTurn() {

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

    private boolean isChoiceCorrect(int choice) {
        if (choice == 0) {
            return true;
        }

        Card stackCard = gameBoard.getStack().getLast();
        Card chosenCard = gameBoard.getPlayers().get(0).getCards().get(choice - 1);

        if (stackCard.getRank() == chosenCard.getRank() || stackCard.getSuit() == chosenCard.getSuit()) {
            return true;
        } else {
            System.out.println("Nie możesz położyć tej karty! Kolor kart lub stopień musi się zgadzać! Jeżeli nie możesz wyłożyć karty, dobierz kartę! ");
            return false;
        }
    }


}
