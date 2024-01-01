import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Card implements Comparable<Card> {
    private int suit;
    private int value;

    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public int compareTo(Card c) {
        if (value == c.value) {
            return Integer.compare(suit, c.suit);
        } else {
            return Integer.compare(value, c.value);
        }
    }

    public String toString() {
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] values = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

        return values[value - 1] + " of " + suits[suit - 1];
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }
}