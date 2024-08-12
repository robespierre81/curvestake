package com.bodiva.curvestake;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    
    public MessageTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetType() {
        System.out.println("getType");
        Message instance = null;
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPayload() {
        System.out.println("getPayload");
        Message instance = null;
        JSONObject expResult = null;
        JSONObject result = instance.getPayload();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testToJson() {
        System.out.println("toJson");
        Message instance = null;
        String expResult = "";
        String result = instance.toJson();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testFromJson() {
        System.out.println("fromJson");
        String jsonString = "";
        Message expResult = null;
        Message result = Message.fromJson(jsonString);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
