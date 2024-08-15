package com.bodiva.curvestake;

import java.security.PublicKey;
import java.util.*;

public class BlackJackContract implements SmartContract {

    private Map<PublicKey, Integer> playerBalances = new HashMap<>(); // Stores each player's balance
    private Map<PublicKey, List<Card>> playerHands = new HashMap<>(); // Stores each player's hand
    private Deck deck = new Deck(); // Represents the deck of cards
    private int betAmount;

    @Override
    public void execute(Hooker transaction) {
        String function = transaction.getFunction();
        PublicKey player = transaction.getSender();

        switch (function) {
            case "placeBet":
                int amount = Integer.parseInt(transaction.getArgs()[0]);
                placeBet(player, amount);
                break;
            case "hit":
                hit(player);
                break;
            case "stand":
                stand(player);
                break;
            default:
                System.out.println("Unknown function: " + function);
        }
    }

    public void placeBet(PublicKey player, int amount) {
        if (playerBalances.getOrDefault(player, 0) < amount) {
            throw new RuntimeException("Insufficient funds to place bet.");
        }
        this.betAmount = amount;
        playerBalances.put(player, playerBalances.get(player) - amount);
        dealInitialCards(player);
    }

    private void dealInitialCards(PublicKey player) {
        List<Card> hand = new ArrayList<>();
        hand.add(deck.draw());
        hand.add(deck.draw());
        playerHands.put(player, hand);
    }

    public void hit(PublicKey player) {
        List<Card> hand = playerHands.get(player);
        hand.add(deck.draw());
        if (getHandValue(hand) > 21) {
            playerBalances.put(player, 0); // Player busts, loses bet
            System.out.println("Player busted.");
        }
    }

    public void stand(PublicKey player) {
        int dealerScore = getDealerScore();
        int playerScore = getHandValue(playerHands.get(player));

        if (playerScore > dealerScore || dealerScore > 21) {
            playerBalances.put(player, playerBalances.get(player) + 2 * betAmount); // Player wins
            System.out.println("Player wins.");
        } else {
            playerBalances.put(player, 0); // Player loses bet
            System.out.println("Player loses.");
        }
    }

    private int getHandValue(List<Card> hand) {
        int value = 0;
        int aces = 0;
        for (Card card : hand) {
            switch (card.getRank()) {
                case "2": case "3": case "4": case "5": case "6":
                case "7": case "8": case "9": case "10":
                    value += Integer.parseInt(card.getRank());
                    break;
                case "J": case "Q": case "K":
                    value += 10;
                    break;
                case "A":
                    aces += 1;
                    value += 11;
                    break;
            }
        }
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    private int getDealerScore() {
        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(deck.draw());
        dealerHand.add(deck.draw());

        int value = getHandValue(dealerHand);
        while (value < 17) {
            dealerHand.add(deck.draw());
            value = getHandValue(dealerHand);
        }
        return value;
    }

    // Allow players to add funds to their balance
    public void addFunds(PublicKey player, int amount) {
        playerBalances.put(player, playerBalances.getOrDefault(player, 0) + amount);
    }
    
    public int getBalance(PublicKey playerKey) {
        return playerBalances.get(playerKey);
    }
}
