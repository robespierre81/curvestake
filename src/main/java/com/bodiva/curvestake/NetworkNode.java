package com.bodiva.curvestake;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkNode {

    private ServerSocket serverSocket;
    private List<Socket> connectedPeers = new ArrayList<>();
    private int port;

    public NetworkNode(int port) {
        this.port = port;
    }

    // Start the server to listen for incoming connections
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            // Continuously listen for incoming connections
            while (true) {
                Socket socket = serverSocket.accept();
                connectedPeers.add(socket);
                System.out.println("New peer connected: " + socket.getRemoteSocketAddress());

                // Start a new thread to handle communication with the connected peer
                new Thread(() -> handlePeerCommunication(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Connect to another peer
    public void connectToPeer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            connectedPeers.add(socket);
            System.out.println("Connected to peer: " + socket.getRemoteSocketAddress());

            // Start a new thread to handle communication with the peer
            new Thread(() -> handlePeerCommunication(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle communication with a connected peer
    private void handlePeerCommunication(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from " + socket.getRemoteSocketAddress() + ": " + message);
                // Handle received messages (e.g., block, transaction)
                processMessage(message);

                // Example: Echo the message back to the sender
                writer.println("Echo: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                connectedPeers.remove(socket);
                System.out.println("Peer disconnected: " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Process incoming messages
    private void processMessage(String message) {
        // Implement logic to process incoming messages
        // For example, you can deserialize JSON messages and update the blockchain state
        System.out.println("Processing message: " + message);
    }

    // Broadcast a message to all connected peers
    public void broadcastMessage(String message) {
        for (Socket peer : connectedPeers) {
            try {
                PrintWriter writer = new PrintWriter(peer.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Stop the server and close all connections
    public void stopServer() {
        try {
            for (Socket peer : connectedPeers) {
                peer.close();
            }
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
