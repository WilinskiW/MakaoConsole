public interface SpecialDecisionMaker {
    Card decide(Rank rank);
     default Card createTempCard(String dana){
        Card card = new Card();
        if(Character.isDigit(dana.charAt(0))){ //J
            Rank rank = Rank.specifyRankInWords(Integer.parseInt(dana));
            card.setRank(rank);
        }
        else {
            Suits suit = Suits.giveSuit(dana); //AS
            card.setSuit(suit);
        }
        return card;
    }
}
