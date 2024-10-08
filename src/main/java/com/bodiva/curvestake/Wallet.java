package com.bodiva.curvestake;

import com.bodiva.curvestake.blockchain.Hooker;
import com.bodiva.curvestake.blockchain.HookerInput;
import com.bodiva.curvestake.blockchain.HookerOutput;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String, HookerOutput> UTXOs = new HashMap<>(); // Unspent transaction outputs

    public Wallet() {
        KeyPair keyPair = ECCUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getAddress() {
        return StringUtil.applySha256(publicKey.toString());
    }

    // Sign a transaction
    public byte[] signTransaction(String data) {
        return ECCUtil.signData(data.getBytes(), privateKey);
    }

    // Generate a new transaction
    public Hooker sendFunds(PublicKey recipient, float value, int gasLimit, float gasPrice) {
        float totalCost = value + (gasLimit * gasPrice);
        float balance = getBalance();
        if (balance < totalCost) {
            System.out.println("Not enough funds. Transaction failed.");
            return null;
        }

        ArrayList<HookerInput> inputs = new ArrayList<>();
        float total = 0;

        // Collect inputs (UTXOs) to cover the total cost
        for (Map.Entry<String, HookerOutput> item : UTXOs.entrySet()) {
            HookerOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new HookerInput(UTXO.getId()));
            if (total >= totalCost) {
                break;
            }
        }

        Hooker transaction = new Hooker(publicKey, recipient, value, gasLimit, gasPrice, new ArrayList<HookerInput>());
        transaction.generateSignature(privateKey);

        // Remove UTXOs that are spent
        for (HookerInput input : inputs) {
            UTXOs.remove(input.getTransactionOutputId());
        }

        return transaction;
    }

    // Calculate the balance by summing the UTXOs
    public float getBalance() {
        float total = 0;
        for (HookerOutput utxo : UTXOs.values()) {
            total += utxo.getValue();
        }
        return total;
    }

    // Collect inputs for the transaction
    private HookerInput[] collectInputs(float value) {
        List<HookerInput> inputs = new ArrayList<>();
        float total = 0;

        for (Map.Entry<String, HookerOutput> entry : UTXOs.entrySet()) {
            HookerOutput UTXO = entry.getValue();
            total += UTXO.getValue();
            inputs.add(new HookerInput(UTXO.getId()));

            if (total >= value) break;
        }

        if (total < value) {
            System.out.println("Not enough funds available.");
            return null; // Not enough funds
        }

        return inputs.toArray(new HookerInput[0]);
    }

    // Update the UTXOs after a transaction
    public void updateUTXOs(Hooker transaction) {
        // Remove the UTXOs that have been spent
        for (HookerInput input : transaction.getInputs()) {
            UTXOs.remove(input.getTransactionOutputId());
        }

        // Add the new UTXOs generated by the transaction
        for (HookerOutput output : transaction.getOutputs()) {
            if (output.isMine(publicKey)) {
                UTXOs.put(output.getId(), output);
            }
        }
    }
    public void receiveFunds(float amount) {
        String transactionId = StringUtil.applySha256("Reward" + publicKey + System.nanoTime());
        HookerOutput rewardOutput = new HookerOutput(publicKey, amount, transactionId);
        UTXOs.put(rewardOutput.getId(), rewardOutput);
    }
}
