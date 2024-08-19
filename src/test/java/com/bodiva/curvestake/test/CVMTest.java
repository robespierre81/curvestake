package com.bodiva.curvestake.test;

import com.bodiva.curvestake.blockchain.Block;
import com.bodiva.curvestake.blockchain.Hooker;
import com.bodiva.curvestake.consensus.ProofOfStake;
import com.bodiva.curvestake.smartcontract.BlackJackContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class CVMTest {

    private CVM cvm;
    private StateManager stateManager;
    private SecurityManager securityManager;
    private ProofOfStake proofOfStake;
    private SmartContract smartContract;
    private Transaction transaction;
    private Block block;

    @BeforeEach
    public void setUp() {
        stateManager = mock(StateManager.class);
        securityManager = mock(SecurityManager.class);
        proofOfStake = mock(ProofOfStake.class);
        smartContract = mock(SmartContract.class);
        transaction = mock(Transaction.class);
        block = mock(Block.class);

        cvm = new CVM(stateManager, securityManager, proofOfStake);
    }

    @Test
    public void testExecuteSmartContract_Success() {
        // Mock behavior
        when(securityManager.verifyContract(smartContract)).thenReturn(true);
        when(transaction.getSmartContract()).thenReturn(smartContract);
        when(smartContract.execute(transaction)).thenReturn(true);

        // Execute smart contract
        boolean result = cvm.executeSmartContract(smartContract, transaction);

        // Verify behavior
        verify(stateManager).loadState(smartContract);
        verify(stateManager).commitState(smartContract);
        assertTrue(result);
    }

    @Test
    public void testExecuteSmartContract_Failure() {
        // Mock behavior
        when(securityManager.verifyContract(smartContract)).thenReturn(true);
        when(transaction.getSmartContract()).thenReturn(smartContract);
        when(smartContract.execute(transaction)).thenReturn(false);

        // Execute smart contract
        boolean result = cvm.executeSmartContract(smartContract, transaction);

        // Verify behavior
        verify(stateManager).loadState(smartContract);
        verify(stateManager, never()).commitState(smartContract);
        assertFalse(result);
    }

    @Test
    public void testExecuteSmartContract_NotVerified() {
        // Mock behavior
        when(securityManager.verifyContract(smartContract)).thenReturn(false);

        // Execute smart contract
        boolean result = cvm.executeSmartContract(smartContract, transaction);

        // Verify behavior
        verify(stateManager, never()).loadState(smartContract);
        verify(stateManager, never()).commitState(smartContract);
        assertFalse(result);
    }

    @Test
    public void testValidateAndExecuteBlock() {
        // Mock behavior
        when(proofOfStake.validateBlock(block)).thenReturn(true);
        when(transaction.isSmartContractTransaction()).thenReturn(true);
        when(block.getTransactions()).thenReturn(Collections.singletonList(transaction));

        // Execute block validation and execution
        cvm.validateAndExecuteBlock(block);

        // Verify behavior
        verify(proofOfStake).validateBlock(block);
        verify(securityManager).verifyContract(smartContract);
        verify(stateManager).loadState(smartContract);
        verify(stateManager).commitState(smartContract);
    }

    @Test
    public void testValidateAndExecuteBlock_InvalidBlock() {
        // Mock behavior
        when(proofOfStake.validateBlock(block)).thenReturn(false);

        // Execute block validation and execution
        cvm.validateAndExecuteBlock(block);

        // Verify no execution occurred
        verify(stateManager, never()).loadState(any(SmartContract.class));
    }
}
