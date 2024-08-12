package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkNodeTest {
    
    public NetworkNodeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testStartServer() {
        System.out.println("startServer");
        NetworkNode instance = null;
        instance.startServer();
        fail("The test case is a prototype.");
    }

    @Test
    public void testConnectToPeer() {
        System.out.println("connectToPeer");
        String ip = "";
        int port = 0;
        NetworkNode instance = null;
        instance.connectToPeer(ip, port);
        fail("The test case is a prototype.");
    }

    @Test
    public void testBroadcastMessage() {
        System.out.println("broadcastMessage");
        String message = "";
        NetworkNode instance = null;
        instance.broadcastMessage(message);
        fail("The test case is a prototype.");
    }

    @Test
    public void testStopServer() {
        System.out.println("stopServer");
        NetworkNode instance = null;
        instance.stopServer();
        fail("The test case is a prototype.");
    }
    
}
