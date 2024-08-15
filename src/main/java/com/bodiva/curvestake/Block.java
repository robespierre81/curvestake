package com.bodiva.curvestake;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private byte[] signature;
    private PublicKey publicKey;
    private int totalGasUsed = 0;
    private int gasLimit = 1000000; // Example limit
    private ArrayList<Hooker> transactions = new ArrayList<>();
    public static Map<String, HookerOutput> UTXOs = new HashMap<>();
    private String minerAddress;
    private Wallet minerWallet;

    public void processTransactions() {
        for (Hooker transaction : transactions) {
            if (transaction.processTransaction()) {
                if (totalGasUsed + transaction.getGasLimit() <= gasLimit) {
                    this.transactions.add(transaction);
                    totalGasUsed += transaction.getGasLimit();

                    // Reward the miner with the transaction's gas fee
                    rewardMiner(transaction.getGasFee());
                } else {
                    System.out.println("Transaction exceeds block gas limit.");
                }
            }
        }
    }

    public Block(String data, String previousHash, PrivateKey privateKey) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
        this.signature = ECCUtil.signData(data.getBytes(), privateKey); // Sign the block
    }
    
    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                data
        );
        return calculatedHash;
    }

    public boolean verifyBlock(PublicKey publicKey) {
        return ECCUtil.verifySignature(data.getBytes(), signature, publicKey);
    }
    
    // Method to get the signature
    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public long getTimeStamp() {
        return timeStamp;
    }
    
    private void rewardMiner(float gasFee) {
        minerWallet.receiveFunds(gasFee);
        System.out.println("Miner rewarded with gas fee: " + gasFee);
    }
    
    public Wallet getMinerWallet() {
        return minerWallet;
    }

    public void setMinerWallet(Wallet minerWallet) {
        this.minerWallet = minerWallet;
    }

    public ArrayList<Hooker> getTransactions() {
        return this.transactions;
    }
}
