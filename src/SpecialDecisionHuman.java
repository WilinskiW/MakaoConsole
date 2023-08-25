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
        Card card = createTempCard(dana);
        if(card.getRank() == null && card.getSuit() == null){
            return decide(rank);
        }
        return card;
    }


}
