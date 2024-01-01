import java.util.ArrayList;
import java.util.Scanner;
public class Game {
    private Player p;
    private Deck cards;

    public Game(String[] testHand) {
        p = new Player();
        cards = new Deck();
        for (String input : testHand) {
            char s = input.charAt(0);
            int suit = 0;
            switch (s) {
                case 'c':
                    suit = 1;
                    break;
                case 'd':
                    suit = 2;
                    break;
                case 'h':
                    suit = 3;
                    break;
                case 's':
                    suit = 4;
                    break;
            }
            int value = Integer.parseInt(input.substring(1));
            Card newCard = new Card(suit, value);
            p.addCard(newCard);
        }
    }

    public Game() {
        p = new Player();
        cards = new Deck();
    }

    public void dealHand() {
        for (int i = 0; i < 5; i++) {
            dealToPlayer();
        }
    }

    public void play() {
        boolean cont = true;
        System.out.println("Welcome to Video Poker!");
        while (cont && p.getBankroll() > 0) {
            System.out.println("YOUR TOKENS: " + p.getBankroll());
            System.out.print("Would you like to play a round? (y/n): ");
            Scanner user = new Scanner(System.in);
            char ch = user.nextLine().toLowerCase().charAt(0);
            cont = true;
            if (ch == 'n') cont = false;
            if (cont) {
                int bet = 0;
                while (bet < 1 || bet > 5 || bet > p.getBankroll()) {
                    System.out.print("How many tokens to bet this hand? (1 to 5): ");
                    bet = user.nextInt();
                    if (bet > p.getBankroll()) {
                        System.out.println("You don't have enough tokens!");
                    }
                }
                if (p.getHand().size() == 0) {
                    cards.reset();
                    dealHand();
                }
                p.bets(bet);
                p.displayHand();
                boolean exchanged = exchangeCards();
                if (exchanged) p.displayHand();
                p.sortHand();
                String result = checkHand(p.getHand());
                System.out.println("You got a " + result + "! ");
                int payout = getPayout(result) * bet;
                System.out.println("PAYOUT: " + payout + " tokens");
                p.winnings(payout);
                p.resetHand();
            }
        }
        if (p.getBankroll() <= 0) {
            System.out.println("Sorry, you are out of tokens. :( ");
        }
        System.out.println("Thank you for playing Video Poker!");
    }

    public boolean exchangeCards() {
        Scanner user = new Scanner(System.in);
        System.out.print("How many cards (0-5) would you like to exchange? ");
        int exch = Integer.parseInt(user.nextLine());
        int i = 0;
        ArrayList<Card> removed = new ArrayList<>();
        int[] positions = new int[exch];
        while (i < exch) {
            System.out.print("Which card (1-5) would you like to exchange? ");
            int pos = Integer.parseInt(user.nextLine());
            boolean cont = true;
            while (cont) {
                cont = false;
                if (pos < 1 || pos > 5) {
                    System.out.println("Card position must be between 1 and 5");
                    System.out.print("Please enter another position: ");
                    pos = Integer.parseInt(user.nextLine());
                    cont = true;
                } else {
                    for (int prev : positions) {
                        if (prev == pos) {
                            System.out.println("You cannot exchange a card that you previously exchanged.");
                            System.out.print("Please enter another position: ");
                            pos = Integer.parseInt(user.nextLine());
                            cont = true;
                        }
                    }
                }
            }
            positions[i] = pos;
            Card rem = p.getCard(pos - 1);
            removed.add(rem);
            i++;
        }
        for (Card rem : removed) {
            p.removeCard(rem);
            dealToPlayer();
        }
        return exch != 0;
    }

    public void dealToPlayer() {
        Card dealt = null;
        boolean alreadyPresent = true;
        while (alreadyPresent) {
            dealt = cards.deal();
            if (dealt.getSuit() == 0 && dealt.getValue() == 0) {
                cards.reset();
                dealt = cards.deal();
            }
            alreadyPresent = false;
            for (Card c : p.getHand()) {
                if (c.compareTo(dealt) == 0) alreadyPresent = true;
            }
        }
        p.addCard(dealt);
    }

    public String checkHand(ArrayList<Card> hand) {
        if (checkRoyalFlush(hand)) return "Royal Flush";
        else if (checkStraightFlush(hand)) return "Straight Flush";
        else if (checkFourOfAKind(hand)) return "Four of a Kind";
        else if (checkFullHouse(hand)) return "Full House";
        else if (checkFlush(hand)) return "Flush";
        else if (checkStraight(hand)) return "Straight";
        else if (checkThreeOfAKind(hand)) return "Three of a Kind";
        else if (checkTwoPairs(hand)) return "Two Pairs";
        else if (checkPair(hand)) return "One Pair";
        else return "No pair";
    }

    public int getPayout(String hand) {
        switch (hand) {
            case "Royal Flush":
                return 250;
            case "Straight Flush":
                return 50;
            case "Four of a Kind":
                return 25;
            case "Full House":
                return 6;
            case "Flush":
                return 5;
            case "Straight":
                return 4;
            case "Three of a Kind":
                return 3;
            case "Two Pairs":
                return 2;
            case "One Pair":
                return 1;
            default:
                return 0;
        }
    }

    public boolean checkStraight(ArrayList<Card> hand) {
        int count = 0;
        for (int i = 1; i < hand.size(); i++) {
            int thisValue = hand.get(i).getValue();
            int prevValue = hand.get(i - 1).getValue();
            if (thisValue == prevValue + 1)
                count++;
        }
        return count == 4 || checkHighAceStraight(hand);
    }

    public boolean checkHighAceStraight(ArrayList<Card> hand) {
        int count = 0;
        for (int i = 2; i < hand.size(); i++) {
            int thisValue = hand.get(i).getValue();
            int prevValue = hand.get(i - 1).getValue();
            if (thisValue == prevValue + 1)
                count++;
        }
        return count == 3 && hand.get(0).getValue() == 1 && hand.get(hand.size() - 1).getValue() == 13;
    }

    public boolean checkFlush(ArrayList<Card> hand) {
        int count = 0;
        for (int i = 1; i < hand.size(); i++) {
            int prevSuit = hand.get(i - 1).getSuit();
            int thisSuit = hand.get(i).getSuit();
            if (prevSuit == thisSuit)
                count++;
        }
        return count == 4;
    }

    public boolean checkRoyalFlush(ArrayList<Card> hand) {
        return checkFlush(hand) && checkHighAceStraight(hand);
    }

    public boolean checkStraightFlush(ArrayList<Card> hand) {
        return checkFlush(hand) && checkStraight(hand);
    }

    public int checkOfAKind(ArrayList<Card> hand, int x) {
        for (int i = 0; i < hand.size() - 1; i++) {
            int count = 1;
            Card current = hand.get(i);
            for (int j = i + 1; j < hand.size(); j++) {
                Card compare = hand.get(j);
                if (current.getValue() == compare.getValue()) {
                    count++;
                }
                if (count == x) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkPair(ArrayList<Card> hand) {
        return checkOfAKind(hand, 2) != -1;
    }

    public boolean checkFourOfAKind(ArrayList<Card> hand) {
        return checkOfAKind(hand, 4) != -1;
    }

    public boolean checkThreeOfAKind(ArrayList<Card> hand) {
        return checkOfAKind(hand, 3) != -1;
    }

    public boolean checkTwoPairs(ArrayList<Card> hand) {
        int pairPos = checkOfAKind(hand, 2);
        if (pairPos < 2) {
            for (int i = pairPos + 2; i < hand.size() - 1; i++) {
                Card thisCard = hand.get(i);
                Card next = hand.get(i + 1);
                if (thisCard.getValue() == next.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkFullHouse(ArrayList<Card> hand) {
        int threeStart = checkOfAKind(hand, 3);
        if (threeStart == 0) {
            Card last = hand.get(hand.size() - 1);
            Card secondLast = hand.get(hand.size() - 2);
            return last.getValue() == secondLast.getValue();
        } else if (threeStart == 2) {
            Card first = hand.get(0);
            Card second = hand.get(1);
            return first.getValue() == second.getValue();
        }
        return false;
    }
}
