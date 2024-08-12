package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;

public class BlockTest {

    private String previousHash;
    private String data;
    private KeyPair keyPair;
    private Block block;

    @BeforeEach
    public void setUp() {
        // Initialize the data and previous hash
        previousHash = "0000000abcd";
        data = "This is a block data";

        // Generate a key pair (private and public keys)
        keyPair = ECCUtil.generateKeyPair();

        // Create a new block
        block = new Block(data, previousHash, keyPair.getPrivate());
    }

    @Test
    public void testCalculateHash() {
        // Calculate the hash manually
        String expectedHash = StringUtil.applySha256(
                previousHash +
                Long.toString(block.getTimeStamp()) +
                data
        );

        // Verify that the block's hash matches the expected hash
        assertEquals(expectedHash, block.calculateHash());
    }

    @Test
    public void testBlockSignature() {
        // Verify the block's signature using the public key
        assertTrue(block.verifyBlock(keyPair.getPublic()), "The block signature should be valid.");
    }

    @Test
    public void testGetSignature() {
        // Verify that the signature is not null
        assertNotNull(block.getSignature(), "The block signature should not be null.");
    }

    @Test
    public void testGetPublicKey() {
        // Since the publicKey field is not set in the constructor, it should be null
        assertNull(block.getPublicKey(), "The public key should be null because it's not set in the constructor.");
    }

    @Test
    public void testBlockHashConsistency() {
        // Ensure that the hash remains consistent if the block's content does not change
        String originalHash = block.calculateHash();
        String recalculatedHash = block.calculateHash();
        assertEquals(originalHash, recalculatedHash, "The block's hash should remain consistent.");
    }

    @Test
    public void testBlockTampering() {
        // Tamper with the block's data
        Block tamperedBlock = new Block("Tampered data", previousHash, keyPair.getPrivate());

        // The signature should now be invalid when verified with the original public key
        assertFalse(tamperedBlock.verifyBlock(keyPair.getPublic()), "The tampered block signature should not be valid.");
    }
}
