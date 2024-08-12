package com.bodiva.curvestake.test;

import com.bodiva.curvestake.CurveStake;
import com.bodiva.curvestake.CurveStakeServer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;

public class CurveStakeTest {

    @Test
    public void testMainMethod() {
        // Use a try-with-resources block to mock the CurveStakeServer constructor and method calls
        try (MockedConstruction<CurveStakeServer> mocked = mockConstruction(CurveStakeServer.class, (mockServer, context) -> {
            // Mock the server methods
            doNothing().when(mockServer).start();
            doNothing().when(mockServer).connectToPeer(anyString(), anyInt());
            doNothing().when(mockServer).initializeBlockchain();
            doNothing().when(mockServer).startServerLoop();
        })) {

            // Call the main method
            CurveStake.main(new String[]{});

            // Get the mock instance of CurveStakeServer from the mocked construction
            CurveStakeServer mockServer = mocked.constructed().get(0);

            // Verify that the methods were called
            verify(mockServer).start();
            verify(mockServer).connectToPeer("127.0.0.1", 2024);
            verify(mockServer).initializeBlockchain();
            verify(mockServer).startServerLoop();
        }
    }
}
