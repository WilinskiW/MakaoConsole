public enum Rank {
    AS(Ability.CHANGE_SUIT), TWO(Ability.PLUS_2), THREE(Ability.PLUS_3), FOUR(Ability.WAIT),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    Q(Ability.ON_EVERYTHING), J(Ability.DEMAND), K(Ability.KING_EXCEPTION);

    public int power;
    public Ability ability;

    Rank(Ability ability) {
        this.ability = ability;
    }

    Rank(int power) {
        this.power = power;
    }
}
