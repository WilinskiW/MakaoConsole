package makao.compHumanProto;

public class ComputerController extends PlayerController {

    public ComputerController(Player player) {
        super(player);
    }

    @Override
    public void printPlayIntro() {

    }

    @Override
    public int selectCard() {
        System.out.println("wybiera karte algorytmem");
        return 0;
    }
}
