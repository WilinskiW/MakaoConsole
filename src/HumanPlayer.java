import java.util.List;

public class HumanPlayer extends Player {
    public HumanPlayer(int id) {
        super(id);
    }

    @Override
    public SpecialDecisionMaker getDecisionMaker() {
        return new SpecialDecisionHuman();
    }
}
