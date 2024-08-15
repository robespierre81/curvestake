package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.HookerInput;
import com.bodiva.curvestake.HookerOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HookerInputTest {

    private HookerInput transactionInput;
    private String transactionOutputId;
    private HookerOutput mockUTXO;

    @BeforeEach
    public void setUp() {
        transactionOutputId = "testOutputId";
        transactionInput = new HookerInput(transactionOutputId);

        // Mock a HookerOutput for testing purposes
        mockUTXO = new HookerOutput(ECCUtil.generateKeyPair().getPublic(), 10.0f, "mockTransactionId");
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
