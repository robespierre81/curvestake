package com.bodiva.curvestake;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Stakeholder {
    private KeyPair keyPair;
    private int stake; // Amount of stake

    public Stakeholder(int stake) {
        this.keyPair = ECCUtil.generateKeyPair();
        this.stake = stake;
    }

    public int getStake() {
        return stake;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}
