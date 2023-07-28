public enum Rank {
    AS(Ability.CHANGE_SUIT), TWO(Ability.PLUS_2), THREE(Ability.PLUS_3), FOUR(Ability.WAIT),
    FIVE(null), SIX(null), SEVEN(null), EIGHT(null), NINE(null), TEN(null),
    Q(Ability.ON_EVERYTHING), J(Ability.DEMAND), K(Ability.KING_EXCEPTION);
    public final Ability ability;

    Rank(Ability ability) {
        this.ability = ability;
    }

    public boolean needsDecision(){
        return this == J || this == AS;
    }

    public static Rank giveNumericRank(int dana) {

        return switch (dana) {
            case 6 -> SIX;
            case 7 -> SEVEN;
            case 8 -> EIGHT;
            case 9 -> NINE;
            case 10 -> TEN;
            default -> null;
        };

      /*  if (dana < 6 || dana > 10) {
            return null;
        }
       return values()[dana-1];*/
    }

}
