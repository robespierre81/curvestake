package com.bodiva.curvestake.test.integration;

import com.bodiva.curvestake.smartcontract.BlackJackContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.crypto.Credentials;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationBlackJackContractTest {

    private Web3j web3j;
    private BlackJackContract blackJackContract;
    private String contractAddress;
    private String ownerAddress;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize Web3j and connect to a local or test Ethereum node
        web3j = Web3j.build(new HttpService("http://127.0.0.1:8545")); // Adjust if using a testnet or mainnet

        // Load your credentials (private key)
        Credentials credentials = Credentials.create("YOUR_PRIVATE_KEY"); // Replace with a test account's private key

        // Deploy the contract and get the contract address
        MessageContract deployedContract = MessageContract.deploy(
                web3j, credentials, new DefaultGasProvider(), "Initial Message").send();
        contractAddress = deployedContract.getContractAddress();

        // Initialize BlackJackContract with the deployed contract address
        blackJackContract = new BlackJackContract(web3j, contractAddress, credentials.getAddress());
    }

    @Test
    public void testSetMessage() throws Exception {
        // Set a new message in the contract
        blackJackContract.setMessage("Hello, CurveStake!");

        // Retrieve the updated message
        String updatedMessage = blackJackContract.getMessage();
        assertEquals("Hello, CurveStake!", updatedMessage);
    }

    @Test
    public void testGetMessage() throws Exception {
        // Retrieve the initial message from the contract
        String initialMessage = blackJackContract.getMessage();
        assertEquals("Initial Message", initialMessage);
    }

    @Test
    public void testTransactionReceipt() throws Exception {
        // Set a new message and capture the transaction receipt
        TransactionReceipt receipt = blackJackContract.contract.setMessage("Testing Transaction").send();
        assertNotNull(receipt.getTransactionHash());
        System.out.println("Transaction hash: " + receipt.getTransactionHash());
    }
}
