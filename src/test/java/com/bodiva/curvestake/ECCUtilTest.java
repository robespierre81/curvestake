package com.bodiva.curvestake;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ECCUtilTest {
    
    public ECCUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGenerateKeyPair() {
        System.out.println("generateKeyPair");
        KeyPair expResult = null;
        KeyPair result = ECCUtil.generateKeyPair();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSignData() {
        System.out.println("signData");
        byte[] data = null;
        PrivateKey privateKey = null;
        byte[] expResult = null;
        byte[] result = ECCUtil.signData(data, privateKey);
        assertArrayEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testVerifySignature() {
        System.out.println("verifySignature");
        byte[] data = null;
        byte[] signature = null;
        PublicKey publicKey = null;
        boolean expResult = false;
        boolean result = ECCUtil.verifySignature(data, signature, publicKey);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
