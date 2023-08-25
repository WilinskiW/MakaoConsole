import java.util.Random;

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

    public static Rank specifyRankInWords(int dana) {

        return switch (dana) {
            case 5 -> FIVE;
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

    public static String specifyRankNumerically(Rank rank){
        return switch (rank){
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            default -> null;
        };
    }

    public static Rank chooseRandomNonFunctionalRank(){
        Random random = new Random();
        return specifyRankInWords(random.nextInt(10-5)+5);
    }

    public static boolean isCardNonFunctional(Rank rank){
        return switch (rank) {
            case FIVE, SIX, SEVEN, EIGHT, NINE, TEN -> true;
            default -> false;
        };
    }

}
