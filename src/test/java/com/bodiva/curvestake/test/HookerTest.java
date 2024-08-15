package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.Hooker;
import com.bodiva.curvestake.Hooker;
import com.bodiva.curvestake.HookerInput;
import com.bodiva.curvestake.HookerInput;
import com.bodiva.curvestake.HookerOutput;
import com.bodiva.curvestake.HookerOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HookerTest {

    private Hooker transaction;
    private KeyPair keyPair;
    private HookerInput[] inputs;

    @BeforeEach
    public void setUp() {
        // Generate a key pair for testing
        keyPair = ECCUtil.generateKeyPair();

        // Mock transaction inputs
        inputs = new HookerInput[]{
                new HookerInput("previousTxOutputId1"),
                new HookerInput("previousTxOutputId2")
        };

        // Create a Hooker object for testing
        transaction = new Hooker(keyPair.getPublic(), keyPair.getPublic(), 10.0f, 21000, 0.0001f, inputs);
    }

    @Test
    public void testGenerateSignature() {
        transaction.generateSignature(keyPair.getPrivate());
        assertNotNull(transaction.verifySignature(), "Transaction signature should be generated and verified successfully.");
    }

    @Test
    public void testVerifySignature() {
        transaction.generateSignature(keyPair.getPrivate());
        assertTrue(transaction.verifySignature(), "Signature verification should pass with the correct public key.");
    }

    @Test
    public void testVerifySignatureWithWrongKey() {
        KeyPair wrongKeyPair = ECCUtil.generateKeyPair();
        transaction.generateSignature(wrongKeyPair.getPrivate());

        assertFalse(transaction.verifySignature(), "Signature verification should fail with the wrong public key.");
    }

    @Test
    public void testCalculateHash() {
        String expectedHash = StringUtil.applySha256(keyPair.getPublic().toString() + keyPair.getPublic().toString() + "10.0" + 21000 + 0.0001f + Arrays.toString(inputs));
        assertEquals(expectedHash, transaction.calculateHash(), "Transaction hash should match the expected value.");
    }

    @Test
    public void testGetGasFee() {
        assertEquals(21000 * 0.0001f, transaction.getGasFee(), 0.00001f, "Gas fee calculation should be correct.");
    }

    @Test
    public void testProcessTransactionWithValidSignature() {
        // Mock the transaction outputs for inputs
        HookerOutput output1 = new HookerOutput(keyPair.getPublic(), 5.0f, "output1");
        HookerOutput output2 = new HookerOutput(keyPair.getPublic(), 10.0f, "output2");

        // Set the UTXO map to return the outputs for the inputs
        Map<String, HookerOutput> utxoMap = new HashMap<>();
        utxoMap.put("previousTxOutputId1", output1);
        utxoMap.put("previousTxOutputId2", output2);
        transaction.setUTXOs(utxoMap);

        transaction.generateSignature(keyPair.getPrivate());
        assertTrue(transaction.processTransaction(), "Transaction should be processed successfully with valid signature.");

        assertEquals(2, transaction.getOutputs().length, "Transaction should have 2 outputs.");
        assertEquals(15.0f, transaction.getInputsValue(), 0.0001f, "Total input value should match the sum of UTXOs.");
    }

    @Test
    public void testProcessTransactionWithInvalidSignature() {
        KeyPair wrongKeyPair = ECCUtil.generateKeyPair();
        transaction.generateSignature(wrongKeyPair.getPrivate());

        assertFalse(transaction.processTransaction(), "Transaction processing should fail with an invalid signature.");
    }

    @Test
    public void testProcessTransactionWithInsufficientFunds() {
        // Mock the transaction outputs for inputs with insufficient funds
        HookerOutput output1 = new HookerOutput(keyPair.getPublic(), 5.0f, "output1");
        HookerOutput output2 = new HookerOutput(keyPair.getPublic(), 4.0f, "output2");

        // Set the UTXO map to return the outputs for the inputs
        Map<String, HookerOutput> utxoMap = new HashMap<>();
        utxoMap.put("previousTxOutputId1", output1);
        utxoMap.put("previousTxOutputId2", output2);
        transaction.setUTXOs(utxoMap);

        transaction.generateSignature(keyPair.getPrivate());
        assertFalse(transaction.processTransaction(), "Transaction processing should fail if the total input value is less than the transaction value.");
    }
}
