package makao.compHumanProto;

public class HumanController extends PlayerController {


    public HumanController(Player player) {
        super(player);
    }

    @Override
    public void printPlayIntro() {
        System.out.println("Zaczyna się Twoja tura");
    }

    @Override
    public int selectCard() {
        System.out.println("Wyswietla opcje");
        System.out.println("Wybierz kartę komunikat");
        System.out.println("wybiera karte scannerem");
        return 0;
    }
}
