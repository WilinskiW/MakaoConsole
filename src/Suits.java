public enum Suits {
    KIER, DIAMOND, TREFL, PIK;

    public static Suits giveSuit(String dana) {
        String danaUpper = dana.toUpperCase();

        return switch (danaUpper) {
            case "KIER" -> KIER;
            case "KARO" -> DIAMOND;
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

    /*
    Hearts - Kier
    Diamond - Karo
    Club - Trefl
    Spade - Pik
     */


}

