package com.bodiva.curvestake.cvm;

import com.bodiva.curvestake.smartcontract.BlackJackContract;
import com.bodiva.curvestake.consensus.ProofOfStake;
import com.bodiva.curvestake.blockchain.Block;
import com.bodiva.curvestake.blockchain.Hooker;

public class CVM {

    private StateManager stateManager;
    private SecurityManager securityManager;
    private ProofOfStake proofOfStake;

    public CVM (StateManager stateManager, SecurityManager securityManager, ProofOfStake proofOfStake) {
        this.stateManager = stateManager;
        this.securityManager = securityManager;
        this.proofOfStake = proofOfStake;
    }

    public boolean executeBlackJackContract(BlackJackContract contract, Hooker transaction) {
        // Ensure that the contract execution is secure and isolated
        if (securityManager.verifyContract(contract)) {
            // Load the contract state
            stateManager.loadState(contract);

            contract.execute(transaction);
            stateManager.commitState(contract);

            return true;
        }
        return false;
    }

    public void validateAndExecuteBlock(Block block) {
        // Only allow execution if the block is validated by PoS
        if (proofOfStake.validateBlock(block)) {
            for (Hooker transaction : block.getTransactions()) {
                if (transaction.isSmartContractTransaction()) {
                    executeBlackJackContract(transaction.getContract(), transaction);
                }
            }
        }
    }
}
