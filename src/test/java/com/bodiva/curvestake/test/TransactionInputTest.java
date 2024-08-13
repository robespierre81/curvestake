package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.TransactionInput;
import com.bodiva.curvestake.TransactionInput;
import com.bodiva.curvestake.TransactionOutput;
import com.bodiva.curvestake.TransactionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionInputTest {

    private TransactionInput transactionInput;
    private String transactionOutputId;
    private TransactionOutput mockUTXO;

    @BeforeEach
    public void setUp() {
        transactionOutputId = "testOutputId";
        transactionInput = new TransactionInput(transactionOutputId);

        // Mock a TransactionOutput for testing purposes
        mockUTXO = new TransactionOutput(ECCUtil.generateKeyPair().getPublic(), 10.0f, "mockTransactionId");
    }

    @Test
    public void testTransactionInputCreation() {
        assertNotNull(transactionInput, "TransactionInput should not be null.");
        assertEquals(transactionOutputId, transactionInput.getTransactionOutputId(), "TransactionOutputId should match the one provided in the constructor.");
    }

    @Test
    public void testGetAndSetUTXO() {
        assertNull(transactionInput.getUTXO(), "UTXO should be null initially.");

        // Set the UTXO and verify
        transactionInput.setUTXO(mockUTXO);
        assertEquals(mockUTXO, transactionInput.getUTXO(), "UTXO should match the one that was set.");
    }
}
