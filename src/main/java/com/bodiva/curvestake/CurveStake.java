package com.bodiva.curvestake;

public class CurveStake {

    public static void main(String[] args) {
        int port = 5000; // Example port number
        CurveStakeServer server = new CurveStakeServer(port);

        // Start the server
        server.start();

        // Optionally, connect to other peers (e.g., other CurveStake servers)
        server.connectToPeer("127.0.0.1", 5001); // Connect to another server

        // Initialize the blockchain
        server.initializeBlockchain();
        
        // Start the server-like application loop
        server.startServerLoop();
    }
}
