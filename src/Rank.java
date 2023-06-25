public enum Rank {
    AS(Ability.CHANGE_SUIT), TWO(Ability.PLUS_2), THREE(Ability.PLUS_3), FOUR(Ability.WAIT),
    FIVE(null), SIX(null), SEVEN(null), EIGHT(null), NINE(null), TEN(null),
    Q(Ability.ON_EVERYTHING), J(Ability.DEMAND), K(Ability.KING_EXCEPTION);
    public final Ability ability;

    Rank(Ability ability) {
        this.ability = ability;
    }

}
