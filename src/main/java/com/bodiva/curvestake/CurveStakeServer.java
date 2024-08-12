package com.bodiva.curvestake;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurveStakeServer {
    private ArrayList<Block> blockchain = new ArrayList<>();
    private ProofOfStake pos = new ProofOfStake();
    private NetworkNode networkNode;

    public CurveStakeServer(int port) {
        networkNode = new NetworkNode(port);
    }

    // Start the server
    public void start() {
        new Thread(() -> networkNode.startServer()).start();
    }

    // Connect to another peer
    public void connectToPeer(String ip, int port) {
        networkNode.connectToPeer(ip, port);
    }

    // Initialize the blockchain with some stakeholders and blocks
    public void initializeBlockchain() {
        System.out.print("Init: ");
        // Create stakeholders
        Stakeholder st1 = new Stakeholder(10);
        Stakeholder st2 = new Stakeholder(20);
        Stakeholder st3 = new Stakeholder(30);

        // Add stakeholders to Proof of Stake
        pos.addStakeholder(st1);
        pos.addStakeholder(st2);
        pos.addStakeholder(st3);

        // Select the validator
        Stakeholder validator = pos.selectValidator();

        // Add blocks to the blockchain
        blockchain.add(new Block("Genesis block", "0", validator.getPrivateKey()));
        blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));
        blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));

        System.out.println(" done");
    }

    // Start the server-like loop
    public void startServerLoop() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Define the task that will run periodically
        Runnable task = () -> {
            // Perform blockchain maintenance or processing
            System.out.println("Server is running. Blockchain is valid: " + isChainValid());
            // Broadcast the current blockchain state or a new block to peers
            networkNode.broadcastMessage("Blockchain status: " + isChainValid());
        };

        // Schedule the task to run every 5 seconds
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);

        // Keep the main thread alive
        try {
            Thread.sleep(Long.MAX_VALUE); // Sleep indefinitely
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Check if the blockchain is valid
    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            if (!currentBlock.verifyBlock(previousBlock.getPublicKey())) {
                System.out.println("Block signature is not valid");
                return false;
            }
        }
        return true;
    }

    public ArrayList<Block> getBlockchain() {
        return this.blockchain;
    }
}
