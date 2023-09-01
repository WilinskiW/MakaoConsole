import java.util.Random;

public enum Rank {
    AS(Ability.CHANGE_SUIT), TWO(Ability.PLUS_2), THREE(Ability.PLUS_3), FOUR(Ability.WAIT),
    FIVE(null), SIX(null), SEVEN(null), EIGHT(null), NINE(null), TEN(null),
    Q(Ability.ON_EVERYTHING), J(Ability.DEMAND), K(Ability.KING_EXCEPTION), JOKER(Ability.WILD_CARD);
    public final Ability ability;

    Rank(Ability ability) {
        this.ability = ability;
    }

    public boolean needsDecision(){
        return this == J || this == AS || this == JOKER;
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

    public static Rank specifyRankInWords(String dana){
        return switch (dana) {
            case "AS"  -> AS;
            case "2" -> TWO;
            case "3" -> THREE;
            case "4" -> FOUR;
            case "5" -> FIVE;
            case "6" -> SIX;
            case "7" -> SEVEN;
            case "8" -> EIGHT;
            case "9" -> NINE;
            case "10" -> TEN;
            case "Q" -> Q;
            case "J" -> J;
            case "K" -> K;
            default -> null;
        };
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

    public static String chooseRankForJoker(){
        Random random = new Random();
        return switch (random.nextInt(4)+1){
            case 1,2,3 -> chooseRandomAttackingRank();
            case 4 -> chooseRandomNonFunctionalRank().name();
            default -> null;
        };
    }

    public static String chooseRandomAttackingRank(){
        Random random = new Random();
        return switch (random.nextInt(3)+1){
            case 1 -> "2";
            case 2 -> "3";
            case 3 -> "K";
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
