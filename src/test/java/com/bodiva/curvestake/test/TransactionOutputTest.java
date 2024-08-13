package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.TransactionOutput;
import com.bodiva.curvestake.TransactionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionOutputTest {

    private TransactionOutput transactionOutput;
    private KeyPair keyPair;
    private String parentTransactionId;
    private float value;

    @BeforeEach
    public void setUp() {
        // Generate a key pair for testing
        keyPair = ECCUtil.generateKeyPair();
        parentTransactionId = "parentTransaction123";
        value = 50.0f;

        // Create a TransactionOutput object for testing
        transactionOutput = new TransactionOutput(keyPair.getPublic(), value, parentTransactionId);
    }

    @Test
    public void testTransactionOutputCreation() {
        assertNotNull(transactionOutput, "TransactionOutput should not be null.");
        assertEquals(value, transactionOutput.getValue(), 0.0001f, "Value should match the one provided in the constructor.");
        assertEquals(parentTransactionId, transactionOutput.getId().substring(transactionOutput.getId().length() - parentTransactionId.length()), "ParentTransactionId should match the one provided in the constructor.");
    }

    @Test
    public void testGetId() {
        String expectedId = StringUtil.applySha256(keyPair.getPublic().toString() + Float.toString(value) + parentTransactionId);
        assertEquals(expectedId, transactionOutput.getId(), "TransactionOutput ID should be correctly calculated using SHA-256.");
    }

    @Test
    public void testIsMine() {
        assertTrue(transactionOutput.isMine(keyPair.getPublic()), "isMine should return true when the public key matches the recipient.");
        
        // Generate a different key pair and check that isMine returns false
        KeyPair differentKeyPair = ECCUtil.generateKeyPair();
        assertFalse(transactionOutput.isMine(differentKeyPair.getPublic()), "isMine should return false when the public key does not match the recipient.");
    }
}
