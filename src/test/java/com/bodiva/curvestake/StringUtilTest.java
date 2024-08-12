package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {
    
    public StringUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testApplySha256() {
        System.out.println("applySha256");
        String input = "";
        String expResult = "";
        String result = StringUtil.applySha256(input);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
