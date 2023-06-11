public class MakaoConsoleDemo {
    public static void main(String[] args) {
        Card card = new Card(Rank.EIGHT, Suits.KIER);
        System.out.println(card);

        GameController gameController = new GameController();

        gameController.start();
    }
}