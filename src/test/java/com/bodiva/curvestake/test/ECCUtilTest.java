package com.bodiva.curvestake.test;

import com.bodiva.curvestake.ECCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class ECCUtilTest {

    private KeyPair keyPair;
    private byte[] data;

    @BeforeEach
    public void setUp() {
        // Generate a key pair for testing
        keyPair = ECCUtil.generateKeyPair();

        // Sample data to be signed
        data = "Sample data for ECC signing".getBytes();
    }

    @Test
    public void testGenerateKeyPair() {
        assertNotNull(keyPair, "KeyPair should not be null");
        assertNotNull(keyPair.getPrivate(), "PrivateKey should not be null");
        assertNotNull(keyPair.getPublic(), "PublicKey should not be null");
    }

    @Test
    public void testSignData() {
        // Sign the data with the private key
        byte[] signature = ECCUtil.signData(data, keyPair.getPrivate());

        assertNotNull(signature, "Signature should not be null");
        assertTrue(signature.length > 0, "Signature length should be greater than 0");
    }

    @Test
    public void testVerifySignature() {
        // Sign the data with the private key
        byte[] signature = ECCUtil.signData(data, keyPair.getPrivate());

        // Verify the signature with the public key
        boolean isVerified = ECCUtil.verifySignature(data, signature, keyPair.getPublic());

        assertTrue(isVerified, "The signature should be verified successfully with the public key");

        // Test with tampered data
        byte[] tamperedData = "Tampered data for ECC signing".getBytes();
        boolean isTamperedVerified = ECCUtil.verifySignature(tamperedData, signature, keyPair.getPublic());

        assertFalse(isTamperedVerified, "The signature should not be verified with tampered data");
    }

    @Test
    public void testVerifySignatureWithWrongKey() {
        // Generate another key pair
        KeyPair anotherKeyPair = ECCUtil.generateKeyPair();

        // Sign the data with the original private key
        byte[] signature = ECCUtil.signData(data, keyPair.getPrivate());

        // Attempt to verify the signature with a different public key
        boolean isVerified = ECCUtil.verifySignature(data, signature, anotherKeyPair.getPublic());

        assertFalse(isVerified, "The signature should not be verified with a different public key");
    }
}
