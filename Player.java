import java.util.Collections;
import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private double bankroll;

    public Player() {
        this.hand = new ArrayList<>();
        this.bankroll = 50;
    }

    public Player(double initialBankroll) {
        this.hand = new ArrayList<>();
        this.bankroll = initialBankroll;
    }

    public void addCard(Card c) {
        hand.add(c);
    }

    public void bets(int amt) {
        bankroll -= amt;
    }

    public void winnings(double odds) {
        bankroll += odds;
    }

    public void resetHand() {
        hand.clear();
    }

    public double getBankroll() {
        return bankroll;
    }

    public void removeCard(Card c) {
        hand.remove(c);
    }

    public void sortHand() {
        Collections.sort(hand);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void displayHand() {
        System.out.print("The hand is:");
        sortHand();
        for (Card c : getHand()) {
            System.out.print("\t" + c);
        }
        System.out.println();
    }

    public Card getCard(int i) {
        return (i >= 0 && i < hand.size()) ? hand.get(i) : null;
    }
}
