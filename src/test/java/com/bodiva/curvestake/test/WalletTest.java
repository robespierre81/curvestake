package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.Transaction;
import com.bodiva.curvestake.Transaction;
import com.bodiva.curvestake.Wallet;
import com.bodiva.curvestake.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    private Wallet wallet;
    private PublicKey recipientPublicKey;

    @BeforeEach
    public void setUp() {
        wallet = new Wallet();
        KeyPair recipientKeyPair = ECCUtil.generateKeyPair();
        recipientPublicKey = recipientKeyPair.getPublic();
    }

    @Test
    public void testWalletCreation() {
        assertNotNull(wallet.getPublicKey(), "Public key should not be null.");
        assertNotNull(wallet.getPrivateKey(), "Private key should not be null.");
        assertEquals(StringUtil.applySha256(wallet.getPublicKey().toString()), wallet.getAddress(), "Wallet address should match the SHA-256 hash of the public key.");
    }

    @Test
    public void testSignTransaction() {
        String data = "This is a test transaction";
        byte[] signature = wallet.signTransaction(data);
        assertTrue(ECCUtil.verifySignature(data.getBytes(), signature, wallet.getPublicKey()), "Signature should be valid when verified with the public key.");
    }

    @Test
    public void testGetBalanceWithNoFunds() {
        assertEquals(0.0f, wallet.getBalance(), 0.0001f, "Balance should be 0 when there are no UTXOs.");
    }

    @Test
    public void testReceiveFunds() {
        wallet.receiveFunds(50.0f);
        assertEquals(50.0f, wallet.getBalance(), 0.0001f, "Balance should be 50.0 after receiving funds.");
    }

    @Test
    public void testSendFundsWithSufficientBalance() {
        wallet.receiveFunds(50.0f);
        int gasLimit = 21000;
        float gasPrice = 0.0001f;
        float amountToSend = 30.0f;

        Transaction transaction = wallet.sendFunds(recipientPublicKey, amountToSend, gasLimit, gasPrice);

        assertNotNull(transaction, "Transaction should be created successfully.");
        assertEquals(1, wallet.getBalance(), 0.0001f, "Remaining balance should match after sending funds, accounting for the gas fee and the amount sent.");
    }

    @Test
    public void testSendFundsWithInsufficientBalance() {
        wallet.receiveFunds(5.0f); // Not enough for the transaction + gas fee
        int gasLimit = 21000;
        float gasPrice = 0.0001f;
        float amountToSend = 10.0f;

        Transaction transaction = wallet.sendFunds(recipientPublicKey, amountToSend, gasLimit, gasPrice);

        assertNull(transaction, "Transaction should fail due to insufficient balance.");
    }

    @Test
    public void testUpdateUTXOs() {
        // Simulate receiving funds and updating UTXOs
        wallet.receiveFunds(50.0f);
        assertEquals(50.0f, wallet.getBalance(), 0.0001f, "Balance should be 50.0 after receiving funds.");

        // Send funds to update the UTXOs
        int gasLimit = 21000;
        float gasPrice = 0.0001f;
        float amountToSend = 20.0f;

        Transaction transaction = wallet.sendFunds(recipientPublicKey, amountToSend, gasLimit, gasPrice);
        assertNotNull(transaction, "Transaction should be created successfully.");

        // Manually call the updateUTXOs method to simulate updating after the transaction
        wallet.updateUTXOs(transaction);

        assertEquals(1, wallet.getBalance(), 0.0001f, "Balance should match after updating UTXOs.");
    }
}
