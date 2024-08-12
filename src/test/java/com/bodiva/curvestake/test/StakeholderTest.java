package com.bodiva.curvestake.test;

import com.bodiva.curvestake.Stakeholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class StakeholderTest {

    private Stakeholder stakeholder;
    private int initialStake;

    @BeforeEach
    public void setUp() {
        initialStake = 100;
        stakeholder = new Stakeholder(initialStake);
    }

    @Test
    public void testStakeholderCreation() {
        assertNotNull(stakeholder, "Stakeholder should not be null.");
    }

    @Test
    public void testGetStake() {
        assertEquals(initialStake, stakeholder.getStake(), "Stake should match the initial stake value.");
    }

    @Test
    public void testGetPublicKey() {
        PublicKey publicKey = stakeholder.getPublicKey();
        assertNotNull(publicKey, "PublicKey should not be null.");
    }

    @Test
    public void testGetPrivateKey() {
        PrivateKey privateKey = stakeholder.getPrivateKey();
        assertNotNull(privateKey, "PrivateKey should not be null.");
    }

    @Test
    public void testKeyPairGenerated() {
        PublicKey publicKey = stakeholder.getPublicKey();
        PrivateKey privateKey = stakeholder.getPrivateKey();

        assertNotNull(publicKey, "PublicKey should not be null.");
        assertNotNull(privateKey, "PrivateKey should not be null.");

        // Check that the public and private keys are indeed a key pair
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        assertEquals(publicKey, keyPair.getPublic(), "Generated public key should match.");
        assertEquals(privateKey, keyPair.getPrivate(), "Generated private key should match.");
    }
}
