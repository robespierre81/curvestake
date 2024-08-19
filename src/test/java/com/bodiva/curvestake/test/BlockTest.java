package com.bodiva.curvestake.test;

import com.bodiva.curvestake.blockchain.Block;
import com.bodiva.curvestake.ECCUtil;
import com.bodiva.curvestake.StringUtil;
import com.bodiva.curvestake.blockchain.Hooker;
import com.bodiva.curvestake.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlockTest {

    private Block block;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Wallet minerWallet;

    @BeforeEach
    public void setUp() {
        KeyPair keyPair = ECCUtil.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        minerWallet = Mockito.mock(Wallet.class);
        block = new Block("Test Block", "previousHash", privateKey);
    }

    @Test
    public void testCalculateHash() {
        String expectedHash = StringUtil.applySha256(
                block.previousHash +
                        Long.toString(block.getTimeStamp()) +
                        "Test Block"
        );

        assertEquals(expectedHash, block.calculateHash());
    }

    @Test
    public void testVerifyBlock() {
        assertTrue(block.verifyBlock(publicKey), "Block signature verification should succeed.");
    }

    @Test
    public void testGetSignature() {
        assertNotNull(block.getSignature(), "Signature should not be null.");
    }

    @Test
    public void testProcessTransactions() {
        Hooker mockTransaction = mock(Hooker.class);
        when(mockTransaction.processTransaction()).thenReturn(true);
        when(mockTransaction.getGasLimit()).thenReturn(21000);
        when(mockTransaction.getGasFee()).thenReturn(0.0001f);
        ArrayList<Hooker> transactions = block.getTransactions();

        block = new Block("Test Block", "previousHash", privateKey);
        block.setMinerWallet(minerWallet);
        transactions.add(mockTransaction);

        block.processTransactions();

        verify(minerWallet, times(1)).receiveFunds(0.0001f);
        assertTrue(transactions.contains(mockTransaction), "The block should contain the processed transaction.");
    }

    @Test
    public void testTransactionExceedsGasLimit() {
        Hooker mockTransaction = mock(Hooker.class);
        when(mockTransaction.processTransaction()).thenReturn(true);
        when(mockTransaction.getGasLimit()).thenReturn(1000001); // Exceeds the gas limit
        when(mockTransaction.getGasFee()).thenReturn(0.0001f);

        block = new Block("Test Block", "previousHash", privateKey);
        block.setMinerWallet(minerWallet);
        block.getTransactions().add(mockTransaction);

        block.processTransactions();

        verify(minerWallet, never()).receiveFunds(anyFloat());
        assertFalse(block.getTransactions().contains(mockTransaction), "The block should not contain the transaction that exceeds the gas limit.");
    }
}
