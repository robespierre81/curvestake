package com.bodiva.curvestake.blockchain;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class HookerReceipt extends TransactionReceipt {
    private String hookerHash;
    private boolean status;

    public HookerReceipt(String hookerHash, boolean status) {
        this.hookerHash = hookerHash;
        this.status = status;
    }

    public String getHookerHash() {
        return hookerHash;
    }

    public void setHookerHash(String hookerHash) {
        this.hookerHash = hookerHash;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
