import java.util.*;

public class GameController {
    private final GameBoard gameBoard = new GameBoard();

    public void start() {
        int amountOfPlayers = decideHowManyPlayers();
        gameBoard.preparPlayers(amountOfPlayers);
        gameBoard.givePlayersStartingCards();
        gameBoard.putStartingCardOnStack();
        playGame(amountOfPlayers);
    }

    private int decideHowManyPlayers() {
        int amountOfPlayers;

        do {
            System.out.println("Podaj ilość graczy (min.2, max.4)");
            amountOfPlayers = readNumber();
        }
        while (amountOfPlayers < 2 || amountOfPlayers > 4);

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
        player.giveCard(gameBoard.getBoardDeck().poll());
        player.setDemanding(false);
        player.setDemanded(false);
        System.out.println("***** Gracz " + (player.getId() + 1) + " dobiera kartę *****");
    }

    private void executeHumanDefenseTurn(Player human, Card stackCard) {
        DefenseController defenseController = new DefenseController();
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

    private int checkHumanChoice(int amountOfCards) {
        int humanChoice;
        do {
            System.out.println("Podaj liczbę od 0 do " + amountOfCards);
            humanChoice = readNumber();
        }
        while (humanChoice < 0 || humanChoice > amountOfCards);
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
            Card chosenCard = player.getCards().get(playerChoice - 1);

            Card decisionCard = new Card();
            if (chosenCard.getRank().needsDecision()) { //Czy karta to J lub AS lub Joker
                decisionCard = player.getDecisionMaker().decide(chosenCard.getRank(), gameBoard.getStack().getLast()); //Utwórz karte
            }
            gameBoard.addCardToStack(chosenCard, player);
            gameBoard.useCardAbility(chosenCard, player.getId(), decisionCard);

            System.out.println("Gracz " + (player.getId() + 1) + " wykłada " + chosenCard);
            if (player.isDemanding()) {
                System.out.println("***** Gracz " + (player.getId() + 1) + " żąda " + gameBoard.getStack().getLast().getRank() + " *****");
            }
            return true;
        }
        return false;
    }

//    private boolean tryRescueAction(Player player) {
//        Card rescueCard = gameBoard.getBoardDeck().peek();
//        player.giveCard(rescueCard);
//        System.out.println("Gracz " + (player.getId() + 1) + " ratuję się " + rescueCard);
//        return true;
//
//    }
//
//
//    private boolean isRescueCardCorrect(){
//        Card stackCard = gameBoard.getStack().getLast();
//        Card rescueCard = gameBoard.getBoardDeck().peek();
//        assert rescueCard != null;
//
//    }

    private boolean executeNeutralOption(Player player) {
        if (gameBoard.getStack().getLast().getRank() == Rank.FOUR) {
            System.out.println("Gracz " + (player.getId() + 1) + " czeka");
            player.setSkipTurnActive(false);
        } else {
            System.out.println("Gracz " + (player.getId() + 1) + " dobiera");
            if (!gameBoard.getPullDeck().isEmpty()) {
                gameBoard.givePlayerPullDeck(player);
            } else {
                player.giveCard(gameBoard.getBoardDeck().poll());
            }
        }
        return true;
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
        DefenseController defenseController = new DefenseController();
        System.out.println("!!!!! Gracz " + (computer.getId() + 1) + " jest atakowany. Karta atakująca to:  " + stackCard + " !!!!!");
        List<Card> defenseCards = defenseController.computerDefenseOption(computer, stackCard);
        if(!defenseCards.isEmpty()){
            System.out.println();
        }
        boolean turnEnded;
        do {
            int defenseChoice = checkComputerChoice(defenseCards,computer);
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

    private int checkComputerChoice(List<Card> validCards,Player computer) {
        if (validCards.isEmpty()) {
            return 0;
        }

        if(computer.isAttacked()){
            return new Random().nextInt(validCards.size()) + 1;
        }
        else {
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
                System.out.println("Wygrywa Gracz " + (player.getId() + 1));
                return true;
            }
        }
        return false;
    }
}
