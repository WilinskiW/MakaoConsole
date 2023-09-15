package makao.compHumanProto;

public abstract class PlayerController {

    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public  void play(){  //template method design pattern
        System.out.println("Tura gracza x");
        System.out.println("różne specjalne akcje");
        int selected = selectCard();
        System.out.println("Wybierz kartę komunikat");
        System.out.println("interpretacja wyboru");
        System.out.println("Gracz zrobił X");
    }

    public abstract void printPlayIntro();
    public abstract int selectCard() ;





}
