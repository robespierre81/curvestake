package com.bodiva.curvestake;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.*;

public class CurveStakeTest {

    @Test
    public void testMainMethod() {
        // Create a mock of CurveStakeServer
        CurveStakeServer mockServer = mock(CurveStakeServer.class);

        // Use a try-with-resources block to mock the CurveStakeServer constructor and method calls
        try (MockedConstruction<CurveStakeServer> mock = mockConstruction(CurveStakeServer.class, (mocked, context) -> {
            // Mock the server methods
            doNothing().when(mocked).start();
            doNothing().when(mocked).connectToPeer(anyString(), anyInt());
            doNothing().when(mocked).initializeBlockchain();
            doNothing().when(mocked).startServerLoop();
        })) {
            // Call the main method
            CurveStake.main(new String[]{});

            // Verify that the methods were called
            verify(mockServer).start();
            verify(mockServer).connectToPeer("127.0.0.1", 5001);
            verify(mockServer).initializeBlockchain();
            verify(mockServer).startServerLoop();
        }
    }
}
