package com.bodiva.curvestake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurveStakeServer {

    private Map<String, SmartContract> smartContracts = new HashMap<>();
    private ArrayList<Block> blockchain = new ArrayList<>();
    private ProofOfStake pos = new ProofOfStake();
    private NetworkNode networkNode;
    private ScheduledExecutorService scheduler;

    public CurveStakeServer(int port) {
        networkNode = new NetworkNode(port);
    }

    // Start the server
    public void start() {
        new Thread(() -> networkNode.startServer()).start();
    }
    
    public void startServerLoop() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            System.out.println("Server is running. Blockchain is valid: " + isChainValid());
            networkNode.broadcastMessage("Blockchain status: " + isChainValid());
        };

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
    
    // Method to stop the server
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();  // Stops the scheduler
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();  // Forcefully stop if not terminated within the time limit
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
        networkNode.stopServer();  // Stops the network node
        System.out.println("Server stopped.");
    }


    // Connect to another peer
    public void connectToPeer(String ip, int port) {
        networkNode.connectToPeer(ip, port);
    }

    // Deploy a smart contract
    public void deploySmartContract(String address, SmartContract contract) {
        smartContracts.put(address, contract);
    }

    // Execute a smart contract
    public void executeSmartContract(String address, HookerTransaction transaction) {
        SmartContract contract = smartContracts.get(address);
        if (contract != null) {
            contract.execute(transaction);
        } else {
            System.out.println("Smart contract not found at address: " + address);
        }
    }

    // Initialize the blockchain with some stakeholders and blocks
    public void initializeBlockchain() {
        System.out.print("Init: ");
        Stakeholder st1 = new Stakeholder(10);
        Stakeholder st2 = new Stakeholder(20);
        Stakeholder st3 = new Stakeholder(30);

        pos.addStakeholder(st1);
        pos.addStakeholder(st2);
        pos.addStakeholder(st3);

        Stakeholder validator = pos.selectValidator();

        blockchain.add(new Block("Genesis block", "0", validator.getPrivateKey()));
        blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));
        blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));

        System.out.println(" done");
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
