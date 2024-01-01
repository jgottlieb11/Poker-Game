import java.util.Random;
public class Deck {
    private Card[] theDeck;
    private int top;

    private static final int NUMBER_OF_CARDS = 52;

    public Deck() {
        theDeck = new Card[NUMBER_OF_CARDS];
        top = 0;
        int count = 0;
        for (int s = 0; s < 4; s++) {
            for (int v = 0; v < 13; v++) {
                theDeck[count] = new Card(s + 1, v + 1);
                count++;
            }
        }
        shuffle();
    }

    public void shuffle() {
        Random r = new Random();
        for (int i = NUMBER_OF_CARDS - 1; i >= 0; i--) {
            int pos = r.nextInt(i + 1);
            Card temp = theDeck[i];
            theDeck[i] = theDeck[pos];
            theDeck[pos] = temp;
        }
    }

    public Card deal() {
        if (top > 51) {
            return new Card(0, 0);
        } else {
            return theDeck[top++];
        }
    }

    public void reset() {
        top = 0;
        shuffle();
    }
}
