public class SpecialDecisionComputer implements SpecialDecisionMaker{
public Player player;

    public SpecialDecisionComputer(Player player) {
        this.player = player;
    }

    @Override
    public Card decide(Rank rank) {
        return null;
    }
}
