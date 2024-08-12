package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurveStakeTest {
    
    public CurveStakeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        CurveStake.main(args);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsChainValid() {
        System.out.println("isChainValid");
        Boolean expResult = null;
        Boolean result = CurveStake.isChainValid();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
