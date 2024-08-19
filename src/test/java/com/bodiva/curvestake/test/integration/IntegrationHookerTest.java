package com.bodiva.curvestake.test.integration;

import com.bodiva.curvestake.smartcontract.BlackJackContract;
import com.bodiva.curvestake.CurveStakeServer;
import com.bodiva.curvestake.blockchain.Hooker;
import com.bodiva.curvestake.blockchain.HookerInput;
import com.bodiva.curvestake.Wallet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationHookerTest {

    private List<CurveStakeServer> servers;
    private List<Wallet> wallets;

    @BeforeEach
    public void setUp() {
        // Initialize five CurveStake servers
        servers = new ArrayList<>();
        wallets = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            CurveStakeServer server = new CurveStakeServer(5000 + i);
            servers.add(server);
            Wallet wallet = new Wallet();
            wallets.add(wallet);
        }

        // Connect all servers to each other
        for (int i = 0; i < servers.size(); i++) {
            for (int j = i + 1; j < servers.size(); j++) {
                servers.get(i).connectToPeer("127.0.0.1", 5000 + j);
                servers.get(j).connectToPeer("127.0.0.1", 5000 + i);
            }
        }
    }

    @AfterEach
    public void tearDown() {
        // Stop all servers after tests
        for (CurveStakeServer server : servers) { 
            server.stop();
        }
    }

    @Test
    public void testTransactionsBetweenServers() {
        // Deploy a BlackJackContract on the first server
        BlackJackContract blackjackContract = new BlackJackContract();
        String blackjackAddress = "blackjack123";
        servers.get(0).deploySmartContract(blackjackAddress, blackjackContract);

        // Fund each wallet with initial balance
        for (Wallet wallet : wallets) {
            blackjackContract.addFunds(wallet.getPublicKey(), 100);
        }

        // Test transactions: each wallet will place a bet and then interact with the BlackJackContract
        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            CurveStakeServer server = servers.get(i);

            // Place a bet
            Hooker betHookerTransaction = new Hooker(wallet.getPublicKey(), null, 0, 0, 0, new HookerInput[0]);
            betHookerTransaction.setFunction("placeBet");
            betHookerTransaction.setArgs(new String[]{"10"});
            server.executeSmartContract(blackjackAddress, betHookerTransaction);

            // Player chooses to hit
            Hooker hitHookerTransaction = new Hooker(wallet.getPublicKey(), null, 0, 0, 0, new HookerInput[0]);
            hitHookerTransaction.setFunction("hit");
            server.executeSmartContract(blackjackAddress, hitHookerTransaction);

            // Player decides to stand
            Hooker standHookerTransaction = new Hooker(wallet.getPublicKey(), null, 0, 0, 0, new HookerInput[0]);
            standHookerTransaction.setFunction("stand");
            server.executeSmartContract(blackjackAddress, standHookerTransaction);

            // Check final balance (should be > 0 if the player won, or 0 if the player lost)
            assertTrue(blackjackContract.getBalance(wallet.getPublicKey()) >= 0, "Balance should be non-negative after the game.");
        }
    }
}
