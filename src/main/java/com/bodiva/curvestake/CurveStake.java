package com.bodiva.curvestake;

public class CurveStake {

    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader("config.properties");
        int port = config.getIntProperty("port");
        CurveStakeServer server = new CurveStakeServer(port);
        
        // Deploy the Blackjack smart contract
        BlackJackContract blackjackContract = new BlackJackContract();
        
        String blackjackAddress = "blackjack123";
        server.deploySmartContract(blackjackAddress, blackjackContract);
        // Start the server
        server.start();

        // Optionally, connect to other peers (e.g., other CurveStake servers)
        server.connectToPeer("127.0.0.1", port); // Connect to another server

        // Initialize the blockchain
        server.initializeBlockchain();
        
        // Start the server-like application loop
        server.startServerLoop();
    }
}
