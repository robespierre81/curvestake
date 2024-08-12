package com.bodiva.curvestake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

public class CurveStakeServerTest {

    private CurveStakeServer curveStakeServer;
    private NetworkNode mockNetworkNode;

    @BeforeEach
    public void setUp() {
        // Mock the NetworkNode class
        mockNetworkNode = mock(NetworkNode.class);

        // Use a constructor that allows dependency injection for testing purposes
        curveStakeServer = new CurveStakeServer(5000) {
            protected NetworkNode createNetworkNode(int port) {
                return mockNetworkNode;
            }
        };
    }

    @Test
    public void testInitializeBlockchain() {
        // Initialize the blockchain
        curveStakeServer.initializeBlockchain();

        // Verify that the blockchain was initialized with three blocks
        ArrayList<Block> blockchain = curveStakeServer.getBlockchain();
        assertNotNull(blockchain);
        assertEquals(3, blockchain.size(), "Blockchain should have 3 blocks after initialization.");

        // Verify that each block is valid
        assertTrue(curveStakeServer.isChainValid(), "Blockchain should be valid after initialization.");
    }

    @Test
    public void testStart() {
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
    public void testIsChainValid() {
        // Initialize the blockchain
        curveStakeServer.initializeBlockchain();

        // Verify that the blockchain is valid
        assertTrue(curveStakeServer.isChainValid(), "Blockchain should be valid.");
    }

    @Test
    public void testStartServerLoop() {
        // We can't test the actual loop easily, so let's just verify that the method runs without exceptions
        assertDoesNotThrow(() -> curveStakeServer.startServerLoop(), "startServerLoop should run without throwing exceptions.");
    }
}
