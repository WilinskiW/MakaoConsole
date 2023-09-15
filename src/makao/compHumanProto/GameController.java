package makao.compHumanProto;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private List<PlayerController> controllers = new ArrayList<>();

    public GameController() {
        createPlayers();
        playLoop();
    }

    public void createPlayers() {
        controllers.add(new HumanController(null));
        controllers.add(new ComputerController(null));
    }
    public void playLoop() {
        while (true) {
            for (PlayerController controller : controllers) {
                controller.play();
            }

        }
    }
}
