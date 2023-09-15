package makao;

import java.util.*;

public class GameController {
    private final GameBoard gameBoard = new GameBoard();
    private final DefenseController defenseController = new DefenseController();
    private static final int MIN_PLAYERS = 2;

    public void start() {
        int amountOfPlayers = decideHowManyPlayers();
        gameBoard.preparePlayers(amountOfPlayers);
        gameBoard.givePlayersStartingCards();
        gameBoard.putStartingCardOnStack();
        playGame(amountOfPlayers);
    }

    private int decideHowManyPlayers() {
        int amountOfPlayers;

        do {
            System.out.println("Podaj ilość graczy (min." + MIN_PLAYERS +", max.4)");
            amountOfPlayers = readNumber();
        }
        while (amountOfPlayers < MIN_PLAYERS || amountOfPlayers > 4);

        return amountOfPlayers;
    }

    private void playGame(int players) {
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
        Card stackCard = gameBoard.getStack().getLast();

        if (human.isAttacked()) {
            executeHumanDefenseTurn(human, stackCard);
        } else if (human.isDemanded() || human.isDemanding()) {
            executeJackAbilityTurn(human, stackCard);
        } else {
            executeHumanNormalTurn(human);
        }
        gameBoard.checkBoardDeckStatus();

        return human.isWinner();
    }


    private void executeJackAbilityTurn(Player player, Card chosenCard) {
        Rank demandedRank = chosenCard.getRank();

        for (Card card : player.getCards()) {
            if (card.getRank().equals(demandedRank)) {
                gameBoard.addCardToStack(card);
                System.out.println("***** Gracz " + (player.getId() + 1) + " wykłada " + demandedRank + " *****");
                player.setDemanded(false);
                player.setDemanding(false);
                return;
            }
        }

        if (isRescueCardCorrect(player)) {
            executeRescueCardAction(player);
        }
        else {
            player.giveCard(gameBoard.getBoardDeck().poll());
            System.out.println("***** Gracz " + (player.getId() + 1) + " dobiera kartę *****");
        }

        player.setDemanding(false);
        player.setDemanded(false);
    }

    private void executeHumanDefenseTurn(Player human, Card stackCard) {
        System.out.println("!!!!! Gracz " + (human.getId() + 1) + " jest atakowany. Karta atakująca to:  " + stackCard + " !!!!!");
        List<Card> defenseCards = defenseController.humanDefenseOption(human, stackCard);
        boolean turnEnded;
        do {
            int defenseChoice = checkHumanChoice(defenseCards.size());
            int playerChoice = 0;
            if (defenseChoice != 0) {
                playerChoice = defenseController.findDefenseCardIndexInPlayerDeck(defenseCards.get(defenseChoice - 1), human);
                playerChoice++;
            }
            turnEnded = executeTurn(playerChoice, human);
        }
        while (!turnEnded);
        human.setAttacked(false);
        endTurnUpdate(human);
    }


    private void executeHumanNormalTurn(Player human) {
        showTurnOptions(human);
        boolean turnEnded;
        do {
            int playerChoice = checkHumanChoice(human.getCards().size());

            turnEnded = executeTurn(playerChoice, human);
        }
        while (!turnEnded);
        endTurnUpdate(human);
    }


    private void showTurnOptions(Player human) {
        System.out.println("///// Tura Gracza " + (human.getId() + 1) + " /////");
        System.out.println("Karta na stosie: " + gameBoard.getStack().getLast());
        System.out.println("0. Dobierz kartę");
        for (int i = 0; i < human.getCards().size(); i++) {
            System.out.println(i + 1 + ". " + human.getCards().get(i));
        }
    }

    private int checkHumanChoice(int lastChoice) {
        int humanChoice;
        do {
            System.out.println("Podaj liczbę opcji:");
            humanChoice = readNumber();
        }
        while (humanChoice < 0 || humanChoice > lastChoice);
        return humanChoice;
    }


    private int readNumber() {
        int number;
        try {
            Scanner scanner = new Scanner(System.in);
            number = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Należy podać liczbę");
            number = readNumber();
        }

        return number;
    }

    private boolean executeTurn(int playerChoice, Player player) { //boolean - czy tura zakończona
        if (playerChoice == 0) { //dobiera
            return executeNeutralOption(player);
        }
        //wybrana karta
        if (isCorrectCard(playerChoice, player.getCards(), player)) { // Czy karta może został położona
            useCard(playerChoice,player);
            return true;
        }
        return false;
    }

    private boolean executeNeutralOption(Player player) {

        if (isRescueCardCorrect(player)) {
           return executeRescueCardAction(player);
        }

        if (gameBoard.getStack().getLast().getRank() == Rank.FOUR) {
            System.out.println("Gracz " + (player.getId() + 1) + " czeka");
            player.setSkipTurnActive(false);
        } else {
            if (!gameBoard.getPullDeck().isEmpty()) {
                System.out.println("Gracz " + (player.getId() + 1) + " dobiera " + gameBoard.getPullDeck().size() + " karty");
                gameBoard.givePlayerPullDeck(player);

            } else {
                player.giveCard(gameBoard.getBoardDeck().poll());
            }
        }
        return true;
    }

    private boolean isRescueCardCorrect(Player player) {
        Card stackCard = gameBoard.getStack().getLast();
        Card rescueCard = gameBoard.getBoardDeck().peek();
        assert rescueCard != null;

        if(!player.isDemanded() && !player.isDemanding()) {
            //Joker
            if (rescueCard.getRank().equals(Rank.JOKER)) {
                return true;
            }
            //2-4, król pik, krol kier
            else if (defenseController.isCardCanBeDefense(rescueCard, stackCard)) {
                return true;
            }
            //AS,5-10, król Trefl, król karo, Q
            else return gameBoard.compareCards(stackCard, rescueCard) || stackCard.getRank().equals(Rank.Q) || rescueCard.getRank().equals(Rank.Q);
        }
        else {
            //J
            return Rank.isCardNonFunctional(stackCard.getRank()) && rescueCard.getRank().equals(stackCard.getRank());
        }
    }

    private boolean executeRescueCardAction(Player player) {
        Card rescueCard = gameBoard.getBoardDeck().poll();
        player.giveCard(rescueCard);
        System.out.println("Gracz " + (player.getId() + 1) + " ratuję się " + rescueCard);
        useCard(player.getCards().size(),player);
        return true;
    }


    private void useCard(int playerChoice,Player player){
        Card chosenCard = player.getCards().get(playerChoice - 1);

        Card decisionCard = new Card();
        if (chosenCard.getRank().needsDecision()) { //Czy karta to J lub AS lub Joker
            decisionCard = player.getDecisionMaker().decide(chosenCard.getRank(), gameBoard.getStack().getLast()); //Utwórz karte
        }
        gameBoard.addCardToStack(chosenCard, player);
        gameBoard.useCardAbility(chosenCard, player.getId(), decisionCard);

        useSpadeKingIfNeeded(player);

        showChosenCardAction(player, chosenCard, decisionCard);
    }

    private void useSpadeKingIfNeeded(Player player){
        Card stackCard = gameBoard.getStack().getLast();
        if(stackCard.getRank() == Rank.K && stackCard.getSuit() == Suits.PIK){
            executeSpadeKingTurn(player.getId(),gameBoard.getPlayers(),stackCard);
        }
    }

    private void executeSpadeKingTurn(int currentPlayerId, List<Player> players,Card stackCard){
        if(currentPlayerId == 0){
            executeComputerDefenseTurn(players.get(players.size()-1),stackCard);
        }
        else if(currentPlayerId == 1){
            executeHumanDefenseTurn(players.get(0),stackCard);
        }
        else {
            executeComputerDefenseTurn(players.get(currentPlayerId - 1),stackCard);
        }
    }


    private void showChosenCardAction(Player player, Card chosenCard, Card decisionCard) {
        if (chosenCard.getRank() == Rank.AS) {
            System.out.println("Gracz " + (player.getId() + 1) + " wykłada " + chosenCard + ". Zmienia kolor na " + decisionCard.getSuit());
        } else {
            System.out.println("Gracz " + (player.getId() + 1) + " wykłada " + chosenCard);
        }

        if (player.getCards().size() == 1) {
            System.out.println("Gracz " + (player.getId() + 1) + " ma MAKAO!!!");
        }

        if (player.isDemanding()) {
            System.out.println("***** Gracz " + (player.getId() + 1) + " żąda " + gameBoard.getStack().getLast().getRank() + " *****");
        }
    }


    private void endTurnUpdate(Player player) {
        gameBoard.setVictoryStatus(player);
        gameBoard.checkBoardDeckStatus();
    }

    private void computerTurns(int players) {
        for (int id = 2; id <= players; id++) {
            if (computerTurn(id)) {
                break;
            }
        }
    }

    private boolean computerTurn(int id) {
        Player computer = gameBoard.getPlayers().get(id - 1);
        Card stackCard = gameBoard.getStack().getLast();

        if (computer.isAttacked()) {
            executeComputerDefenseTurn(computer, stackCard);
        } else if (computer.isDemanded() || computer.isDemanding()) {
            executeJackAbilityTurn(computer, stackCard);
        } else {
            executeComputerNormanlTurn(computer);
        }
        gameBoard.checkBoardDeckStatus();

        return computer.isWinner();
    }

    private void executeComputerDefenseTurn(Player computer, Card stackCard) {
        System.out.println("!!!!! Gracz " + (computer.getId() + 1) + " jest atakowany. Karta atakująca to:  " + stackCard + " !!!!!");
        List<Card> defenseCards = defenseController.computerDefenseOption(computer, stackCard);
        if (!defenseCards.isEmpty()) {
            System.out.println();
        }
        boolean turnEnded;
        do {
            int defenseChoice = checkComputerChoice(defenseCards, computer);
            int computerChoice = 0;
            if (defenseChoice != 0) {
                computerChoice = defenseController.findDefenseCardIndexInPlayerDeck(defenseCards.get(defenseChoice - 1), computer);
                computerChoice++;
            }
            turnEnded = executeTurn(computerChoice, computer);
        }
        while (!turnEnded);
        computer.setAttacked(false);
        endTurnUpdate(computer);
    }

    private void executeComputerNormanlTurn(Player computer) {
        showComputerInformation(gameBoard.getStack().getLast(), computer, computer.getAmountsOfCards());

        List<Card> validCards = findValidCards(computer.getAmountsOfCards(), computer, gameBoard.getStack().getLast());
        boolean turnEnded;

        do {
            int playerChoice = checkComputerChoice(validCards, computer);
            turnEnded = executeTurn(playerChoice, computer);
        }
        while (!turnEnded);
        endTurnUpdate(computer);
    }

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

        if (computer.isAttacked()) {
            return new Random().nextInt(validCards.size()) + 1;
        } else {
            return new Random().nextInt(computer.getCards().size()) + 1;
        }
    }


    private boolean isCorrectCard(int choice, List<Card> playerCards, Player player) {
        Card stackCard = gameBoard.getStack().getLast();
        Card chosenCard = player.getCards().get(choice - 1);

        if (stackCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("JOKER")) {
            return true;
        }

        if (choice == 0 || choice > playerCards.size()) {
            return false;
        }

        return gameBoard.compareCards(chosenCard, stackCard);
    }

    private boolean isVictoryAchieve() {
        for (Player player : gameBoard.getPlayers()) {
            if (player.isWinner()) {
                System.out.println("I po Makale. Wygrywa Gracz " + (player.getId() + 1));
                return true;
            }
        }
        return false;
    }
}
