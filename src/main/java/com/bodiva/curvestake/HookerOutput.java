package com.bodiva.curvestake;

import java.security.PublicKey;

public class HookerOutput {
    private String id;
    private PublicKey recipient; // The owner of this output
    private float value; // The amount of coins
    private String parentTransactionId; // The transaction that created this output

    public HookerOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(recipient.toString() + Float.toString(value) + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return publicKey.equals(recipient);
    }

    public float getValue() {
        return value;
    }
    
    public String getId() {
        return id;
    }
}
