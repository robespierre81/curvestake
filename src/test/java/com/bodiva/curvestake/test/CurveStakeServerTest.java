package com.bodiva.curvestake.test;

import com.bodiva.curvestake.CurveStakeServer;
import com.bodiva.curvestake.CurveStakeServer;
import com.bodiva.curvestake.NetworkNode;
import com.bodiva.curvestake.NetworkNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

public class CurveStakeServerTest {

    private CurveStakeServer curveStakeServer;
    private NetworkNode mockNetworkNode;
    private ScheduledExecutorService mockScheduler;

    @BeforeEach
    public void setUp() {
        // Mock the NetworkNode class
        mockNetworkNode = mock(NetworkNode.class);
        mockScheduler = mock(ScheduledExecutorService.class);

        // Use the constructor that accepts a scheduler for testing purposes
        curveStakeServer = new CurveStakeServer(5000);
    }

    @Test
    public void testStart() {
        // Mock the startServer method to avoid starting a real server
        doNothing().when(mockNetworkNode).startServer();

        // Start the server
        curveStakeServer.start();

        // Verify that the startServer method was called on the network node
        verify(mockNetworkNode, times(1)).startServer();
    }

    @Test
    public void testConnectToPeer() {
        // Connect to a peer
        curveStakeServer.connectToPeer("127.0.0.1", 5001);

        // Verify that the connectToPeer method was called with the correct arguments
        verify(mockNetworkNode, times(1)).connectToPeer("127.0.0.1", 5001);
    }

    @Test
    public void testInitializeBlockchain() {
        // Call the method to initialize the blockchain
        curveStakeServer.initializeBlockchain();

        // Verify that the blockchain was initialized with three blocks
        assertEquals(3, curveStakeServer.getBlockchain().size(), "Blockchain should have 3 blocks after initialization.");
    }

    @Test
    public void testStartServerLoop() {
        // Start the server loop
        curveStakeServer.startServerLoop();

        // Verify that the scheduler's scheduleAtFixedRate method was called
        verify(mockScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(5L), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testIsChainValid() {
        // Initialize the blockchain
        curveStakeServer.initializeBlockchain();

        // Verify that the blockchain is valid
        assertTrue(curveStakeServer.isChainValid(), "Blockchain should be valid after initialization.");
    }

    @Test
    public void testIsChainInvalid() {
        // Initialize the blockchain
        curveStakeServer.initializeBlockchain();

        // Tamper with the blockchain by changing the hash of the first block
        curveStakeServer.getBlockchain().get(0).hash = "invalid_hash";

        // Verify that the blockchain is now invalid
        assertFalse(curveStakeServer.isChainValid(), "Blockchain should be invalid after tampering.");
    }
}
