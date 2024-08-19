package com.bodiva.curvestake;

import com.bodiva.curvestake.smartcontract.BlackJackContract;
import com.bodiva.curvestake.blockchain.HookerReceipt;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class BlackJackLoader {

    private Web3j web3j;
    private String contractAddress;
    private String ownerAddress;
    private BlackJackContract contract;

// No-argument constructor that initializes BlackJackContract internally
    public BlackJackLoader() {
        this.contract = new BlackJackContract(); // Initialize with default settings
    }

    // Constructor that initializes the BlackJackContract instance
    public BlackJackLoader(BlackJackContract contract) {
        this.contract = contract;
    }

    public BlackJackLoader(String providerUrl, String contractAddress, String ownerAddress) {
        this.web3j = Web3j.build(new HttpService(providerUrl));
        this.contractAddress = contractAddress;
        this.ownerAddress = ownerAddress;
        loadContract();
    }

    public void setMessage(String newMessage) {
        HookerReceipt transactionReceipt = contract.setMessage(newMessage);
        System.out.println("Transaction hash: " + transactionReceipt.getHookerHash());
    }

    public String getMessage() {
        return contract.getMessage();
    }

    private void loadContract() {
        // Load the smart contract using Web3j
        ContractGasProvider gasProvider = new DefaultGasProvider();
        this.contract = BlackJackContract.load(
                contractAddress, web3j,
                new ClientTransactionManager(web3j, ownerAddress),
                gasProvider
        );
    }

    public static void main(String[] args) {
        try {
            BlackJackLoader loader = new BlackJackLoader();
            String currentMessage = loader.getMessage();
            System.out.println("Current message: " + currentMessage);

            loader.setMessage("Hello, CurveStake!");

            String updatedMessage = loader.getMessage();
            System.out.println("Updated message: " + updatedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
