import java.util.Scanner;

public class SpecialDecisionHuman implements SpecialDecisionMaker {
    public Card decide(Rank rank, Card stackCard){
        Scanner scanner = new Scanner(System.in);

        switch (rank){
            case J -> System.out.println("Podaj rządaną rangę od 5-10"); // figura
            case AS -> System.out.println("Podaj rządany kolor"); //kolor
            case JOKER -> System.out.println(giveInformationAboutJoker(stackCard)); //wszystko
        }

        String dana = scanner.nextLine();
        Card card = createTempCard(dana, rank, stackCard);
        if(card.getRank() == null && card.getSuit() == null){
            return decide(rank, stackCard);
        }
        return card;
    }

    private String giveInformationAboutJoker(Card stackCard){
        if(stackCard.getRank() != Rank.Q){
            return "Musisz wybrać rangę lub kolor, którą chcesz zmienić. Jeżeli wybierzesz jedno z nich, drugie pozostanie bez zmian" + "\n"
                    + "Rangi: AS, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K ; Kolor: Kier, Karo, Pik, Trefl";
        }
        else {
            return "Podaj rangę i kolor w formacie ( RANK SUIT)";
        }
    }

}
