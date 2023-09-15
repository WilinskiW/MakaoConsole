package makao;

import java.util.Random;

public enum Suits {
    KIER, KARO, TREFL, PIK;

    public static Suits giveSuit(String dana) {
        String danaUpper = dana.toUpperCase();

        return switch (danaUpper) {
            case "KIER" -> KIER;
            case "KARO" -> KARO;
            case "TREFL" -> TREFL;
            case "PIK" -> PIK;
            default -> null;
        };
        /*for (Suits value : values()) {
            if (value.name().equals(danaUpper)) {
                return value;
            }
        }*/
    }

    public static String giveRandomSuit() {
        Random random = new Random();
        Suits[] suits = values();
        return suits[random.nextInt(suits.length)].name();
    }

    public static String giveAttackingKing() {
        String randomSuit = giveRandomSuit();
        if (randomSuit.equals("KIER") || randomSuit.equals("PIK")) {
            return randomSuit;
        } else {
            return giveAttackingKing();
        }
    }

    /*
    Hearts - Kier
    Diamond - Karo
    Club - Trefl
    Spade - Pik
     */


}

