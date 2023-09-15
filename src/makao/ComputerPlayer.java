package makao;

public class ComputerPlayer extends Player {
    public ComputerPlayer(int id) {
        super(id);
    }

    @Override
    public SpecialDecisionMaker getDecisionMaker() {
        return new SpecialDecisionComputer(this);
    }
}
