package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProofOfStakeTest {
    
    public ProofOfStakeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAddStakeholder() {
        System.out.println("addStakeholder");
        Stakeholder stakeholder = null;
        ProofOfStake instance = new ProofOfStake();
        instance.addStakeholder(stakeholder);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSelectValidator() {
        System.out.println("selectValidator");
        ProofOfStake instance = new ProofOfStake();
        Stakeholder expResult = null;
        Stakeholder result = instance.selectValidator();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
