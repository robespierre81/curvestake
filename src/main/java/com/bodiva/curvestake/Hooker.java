package com.bodiva.curvestake;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Hooker {
    private String transactionId; // Hash of the transaction
    private PublicKey sender; // Sender's public key
    private PublicKey recipient; // Recipient's public key
    private float value; // Amount being sent
    private byte[] signature; // Signature to prevent tampering
    private float gasPrice; // Price per gas unit
    private int gasLimit; // Maximum amount of gas the sender is willing to pay
    private Map<String, HookerOutput> UTXOs = new HashMap<>(); // Unspent transaction outputs

    private HookerInput[] inputs; // Inputs to this transaction
    private HookerOutput[] outputs; // Outputs to this transaction
    private String function; // Function name for smart contract
    private String[] args; // Arguments for the function

    // Constructor
    public Hooker(PublicKey sender, PublicKey recipient, float value, int gasLimit, float gasPrice, HookerInput[] inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
        this.inputs = inputs;
        this.outputs = new HookerOutput[1]; // Simplified for brevity
    }

    // Generate the transaction's signature
    public void generateSignature(PrivateKey privateKey) {
        String data = sender.toString() + recipient.toString() + Float.toString(value) + gasLimit + gasPrice;
        signature = ECCUtil.signData(data.getBytes(), privateKey);
    }

    // Verify the transaction's signature
    public boolean verifySignature() {
        String data = sender.toString() + recipient.toString() + Float.toString(value) + gasLimit + gasPrice;
        return ECCUtil.verifySignature(data.getBytes(), signature, sender);
    }

    // Calculate the transaction's hash (ID)
    public String calculateHash() {
        return StringUtil.applySha256(sender.toString() + recipient.toString() + Float.toString(value) + gasLimit + gasPrice + Arrays.toString(inputs));
    }

    // Calculate the total gas fee
    public float getGasFee() {
        return gasLimit * gasPrice;
    }
    
    public int getGasLimit() {
        return gasLimit;
    }
    
    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("Transaction signature verification failed.");
            return false;
        }

        // Gather inputs and ensure they are unspent
        for (HookerInput input : inputs) {
            input.setUTXO(UTXOs.get(input.getTransactionOutputId()));
        }

        // Ensure the transaction inputs are valid
        if (getInputsValue() < value) {
            System.out.println("Transaction inputs are less than the value.");
            return false;
        }

        // Generate transaction outputs
        float leftover = getInputsValue() - value;
        transactionId = calculateHash();
        outputs = new HookerOutput[]{
                new HookerOutput(this.recipient, value, transactionId),
                new HookerOutput(this.sender, leftover, transactionId)
        };

        // Add outputs to the UTXO map
        for (HookerOutput output : outputs) {
            UTXOs.put(output.getId(), output);
        }

        // Remove spent inputs from the UTXO map
        for (HookerInput input : inputs) {
            if (input.getUTXO() != null) {
                UTXOs.remove(input.getUTXO().getId());
            }
        }

        return true;
    }
    
    // Returns the sum of the input values (UTXOs) used in this transaction
    public float getInputsValue() {
        float total = 0;
        for (HookerInput input : inputs) {
            if (input.getUTXO() != null) {
                total += input.getUTXO().getValue();
            }
        }
        return total;
    }

    // Get the inputs of the transaction
    public HookerInput[] getInputs() {
        return inputs;
    }

    // Get the outputs of the transaction
    public HookerOutput[] getOutputs() {
        return outputs;
    }
    
    public Map<String, HookerOutput> getUTXOs() {
        return UTXOs;
    }
    
    public void setUTXOs(Map<String, HookerOutput> newUTXOs) {
        UTXOs = newUTXOs;
    }
    
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
    
    public PublicKey getSender() {
        return sender;
    }
    
    // Method to simulate sending the transaction to the smart contract
    public CompletableFuture<HookerReceipt> send() {
        // Simulate sending the transaction and getting a receipt
        String hash = "0x123456789abcdef"; // Example hash
        boolean success = true; // Example success status

        HookerReceipt receipt = new HookerReceipt(hash, success);
        return CompletableFuture.completedFuture(receipt);
    }

    // Example methods to simulate getting and setting messages in the contract
    public CompletableFuture<String> getMessage() {
        // Simulate retrieving the message from the contract
        return CompletableFuture.completedFuture("Hello, CurveStake!");
    }

    public CompletableFuture<HookerReceipt> setMessage(String newMessage) {
        // Simulate setting the message in the contract and returning a receipt
        String hash = "0xabcdef123456789"; // Example hash
        boolean success = true; // Example success status

        HookerReceipt receipt = new HookerReceipt(hash, success);
        return CompletableFuture.completedFuture(receipt);
    }
}
