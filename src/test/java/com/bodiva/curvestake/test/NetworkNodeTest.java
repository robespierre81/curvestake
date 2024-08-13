package com.bodiva.curvestake.test;

import com.bodiva.curvestake.Message;
import com.bodiva.curvestake.Message;
import com.bodiva.curvestake.NetworkNode;
import com.bodiva.curvestake.NetworkNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;
import static org.mockito.Mockito.*;

public class NetworkNodeTest {

    private NetworkNode networkNode;
    private ServerSocket mockServerSocket;
    private Socket mockSocket;
    private PrintWriter mockWriter;
    private BufferedReader mockReader;

    @BeforeEach
    public void setUp() throws Exception {
        mockServerSocket = mock(ServerSocket.class);
        mockSocket = mock(Socket.class);
        mockWriter = mock(PrintWriter.class);
        mockReader = mock(BufferedReader.class);

        networkNode = new NetworkNode(2024) {
            protected ServerSocket createServerSocket(int port) throws IOException {
                return mockServerSocket;
            }

            protected Socket createSocket(String ip, int port) throws IOException {
                return mockSocket;
            }

            protected BufferedReader createBufferedReader(InputStream inputStream) throws IOException {
                return mockReader;
            }

            protected PrintWriter createPrintWriter(OutputStream outputStream) throws IOException {
                return mockWriter;
            }
        };

        when(mockServerSocket.accept()).thenReturn(mockSocket);
        when(mockSocket.getInputStream()).thenReturn(mock(InputStream.class));
        when(mockSocket.getOutputStream()).thenReturn(mock(OutputStream.class));
    }

    @AfterEach
    public void tearDown() throws Exception {
        networkNode.stopServer();
    }

    @Test
    public void testStartServer() throws Exception {
        doNothing().when(mockSocket).close();

        new Thread(() -> networkNode.startServer()).start();
        Thread.sleep(100); // Wait for server to start

        verify(mockServerSocket, times(1)).accept();
    }

    @Test
    public void testConnectToPeer() throws Exception {
        doNothing().when(mockSocket).close();

        networkNode.connectToPeer("127.0.0.1", 2024);

        verify(mockSocket, times(1)).getOutputStream();
        verify(mockSocket, times(1)).getInputStream();
        verify(mockSocket, times(1)).close();
    }

    @Test
    public void testHandlePeerCommunication() throws Exception {
        when(mockReader.readLine()).thenReturn("{\"type\":\"PING\",\"payload\":{}}", (String) null);

        new Thread(() -> networkNode.startServer()).start();
        Thread.sleep(100); // Wait for server to start

        verify(mockReader, atLeastOnce()).readLine();
        verify(mockWriter, atLeastOnce()).println(anyString());
    }

    @Test
    public void testProcessBlockMessage() throws Exception {
        JSONObject payload = new JSONObject().put("block", "sample_block");
        Message message = new Message("BLOCK", payload);
        networkNode.processMessage(message);

        // You would typically verify internal state changes or interactions
        // Here, we are just checking the console output for simplicity
        // In a real-world scenario, use logging frameworks or other verification methods
        System.out.println("Processed block message");
    }

    @Test
    public void testProcessTransactionMessage() throws Exception {
        JSONObject payload = new JSONObject().put("transaction", "sample_transaction");
        Message message = new Message("TRANSACTION", payload);
        networkNode.processMessage(message);

        System.out.println("Processed transaction message");
    }

    @Test
    public void testBroadcastMessage() throws Exception {
        networkNode.connectToPeer("127.0.0.1", 2024);
        networkNode.broadcastMessage("{\"type\":\"BROADCAST\",\"payload\":{}}");

        verify(mockWriter, times(1)).println("{\"type\":\"BROADCAST\",\"payload\":{}}");
    }

    @Test
    public void testStopServer() throws Exception {
        doNothing().when(mockSocket).close();
        networkNode.connectToPeer("127.0.0.1", 2024);

        networkNode.stopServer();

        verify(mockSocket, times(1)).close();
        verify(mockServerSocket, times(1)).close();
    }
}
