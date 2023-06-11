public class GameController {
    private GameBoard gameBoard;
    public void start(){
        System.out.println("Zaczyna się grać");
        GameBoard gameBoard = new GameBoard();
        int playersCount = decideHowManyPlayers();
        gameBoard.preparPlayers(playersCount);
        System.out.println("Przygotowano " + playersCount + " graczy");
        System.out.println("Rozdano każdemu po 5 kart");
        gameBoard.givePlayersStartingCards();
        System.out.println(gameBoard);
    }

    private void playTurn(){

    }

    private int decideHowManyPlayers(){
        System.out.println("Podaj ilość graczy:");

        return 4;
    }


}
