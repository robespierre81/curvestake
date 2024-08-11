package com.bodiva.curvestake;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurveStake {
    private static ArrayList<Block> blockchain = new ArrayList<>();
    private static ProofOfStake pos = new ProofOfStake();

    public static void main(String[] args) {
        // Initialize the server-like application
        initializeBlockchain();
        startServer();
    }

    // Method to initialize the blockchain with some stakeholders and blocks
    private static void initializeBlockchain() {
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
        
        System.out.print(" done");
    }

    // Method to start the server-like loop
    private static void startServer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Define the task that will run periodically
        Runnable task = () -> {
            // Perform blockchain maintenance or processing
            System.out.println("Server is running. Blockchain is valid: " + isChainValid());
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

    // Method to check if the blockchain is valid
    public static Boolean isChainValid() {
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
}
