import java.util.Scanner;

public class SpecialDecisionHuman implements SpecialDecisionMaker {
    public Card decide(Rank rank){
        Scanner scanner = new Scanner(System.in);

        switch (rank){
            case J -> System.out.println("Podaj rządaną rangę od 5-10"); // figura
            case AS -> System.out.println("Podaj rządany kolor"); //kolor
            //case Joker -> System.out.println("Joker");
        }

        String dana = scanner.nextLine();
        return createCard(dana);
    }

    private Card createCard(String dana){
        Card card = new Card();
        if(Character.isDigit(dana.charAt(0))){
            Rank rank = Rank.giveNumericRank(Integer.parseInt(dana));
            card.setRank(rank);
        }
        else {
            Suits suit = Suits.giveSuit(dana);
            card.setSuit(suit);
        }
        return card;
    }


}
