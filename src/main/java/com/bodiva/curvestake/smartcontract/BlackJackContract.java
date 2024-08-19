package com.bodiva.curvestake.smartcontract;

import com.bodiva.curvestake.Card;
import com.bodiva.curvestake.Deck;
import com.bodiva.curvestake.SmartContract;
import com.bodiva.curvestake.blockchain.Hooker;
import com.bodiva.curvestake.blockchain.HookerReceipt;
import java.security.PublicKey;
import java.util.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.DefaultGasProvider;

public class BlackJackContract extends Contract implements SmartContract {

    private Map<PublicKey, Integer> playerBalances = new HashMap<>(); // Stores each player's balance
    private Map<PublicKey, List<Card>> playerHands = new HashMap<>(); // Stores each player's hand
    private Deck deck = new Deck(); // Represents the deck of cards
    private int betAmount;
    private BlackJackContract contract;
    
    private static final String DEFAULT_CONTRACT_ADDRESS = "0xYourDefaultContractAddress";
    private static final String DEFAULT_PROVIDER_URL = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID";
    private static final String DEFAULT_OWNER_ADDRESS = "0xYourDefaultOwnerAddress";

    // Default Constructor
    public BlackJackContract() {
        this(
            DEFAULT_CONTRACT_ADDRESS,
            Web3j.build(new HttpService(DEFAULT_PROVIDER_URL)),
            new ClientTransactionManager(Web3j.build(new HttpService(DEFAULT_PROVIDER_URL)), DEFAULT_OWNER_ADDRESS),
            new DefaultGasProvider()
        );
    }
    
    protected BlackJackContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, credentials, gasProvider);
    }

    protected BlackJackContract(String contractAddress, Web3j web3j, ClientTransactionManager transactionManager, ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, transactionManager, gasProvider);
    }



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
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                    value += Integer.parseInt(card.getRank());
                    break;
                case "J":
                case "Q":
                case "K":
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
    }// Method to set a message in the smart contract

    public HookerReceipt setMessage(String newMessage) {
        // Simulate sending a transaction to the blockchain
        String hash = "0xabcdef123456789";  // Example transaction hash
        boolean success = true;  // Simulate success

        // Create and return a HookerReceipt
        return new HookerReceipt(hash, success);
    }

    // Method to get a message from the smart contract
    public String getMessage() {
        // Simulate retrieving a message from the smart contract
        return "Hello, CurveStake!";
    }
    
    // Load method to instantiate the contract
    public static BlackJackContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new BlackJackContract(contractAddress, web3j, credentials, gasProvider);
    }

    public static BlackJackContract load(String contractAddress, Web3j web3j, ClientTransactionManager transactionManager, ContractGasProvider gasProvider) {
        return new BlackJackContract(contractAddress, web3j, transactionManager, gasProvider);
    }
}
