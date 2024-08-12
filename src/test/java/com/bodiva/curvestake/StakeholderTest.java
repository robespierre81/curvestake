package com.bodiva.curvestake;

import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StakeholderTest {
    
    public StakeholderTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetStake() {
        System.out.println("getStake");
        Stakeholder instance = null;
        int expResult = 0;
        int result = instance.getStake();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPublicKey() {
        System.out.println("getPublicKey");
        Stakeholder instance = null;
        PublicKey expResult = null;
        PublicKey result = instance.getPublicKey();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPrivateKey() {
        System.out.println("getPrivateKey");
        Stakeholder instance = null;
        PrivateKey expResult = null;
        PrivateKey result = instance.getPrivateKey();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
