package com.bodiva.curvestake;

public class HookerInput {
    private String transactionOutputId; // Reference to HookerOutput -> transactionId
    private HookerOutput UTXO; // Contains the unspent transaction output

    public HookerInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public HookerOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(HookerOutput UTXO) {
        this.UTXO = UTXO;
    }
}
