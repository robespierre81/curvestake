package com.bodiva.curvestake;

import java.security.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {
    
    public BlockTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalculateHash() {
        System.out.println("calculateHash");
        Block instance = null;
        String expResult = "";
        String result = instance.calculateHash();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testVerifyBlock() {
        System.out.println("verifyBlock");
        PublicKey publicKey = null;
        Block instance = null;
        boolean expResult = false;
        boolean result = instance.verifyBlock(publicKey);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSignature() {
        System.out.println("getSignature");
        Block instance = null;
        byte[] expResult = null;
        byte[] result = instance.getSignature();
        assertArrayEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPublicKey() {
        System.out.println("getPublicKey");
        Block instance = null;
        PublicKey expResult = null;
        PublicKey result = instance.getPublicKey();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
