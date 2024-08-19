package com.bodiva.curvestake;

import com.bodiva.curvestake.network.Message;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class NetworkNode {

    private ServerSocket serverSocket;
    private List<Socket> connectedPeers = new ArrayList<>();
    private int port;

    public NetworkNode(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                connectedPeers.add(socket);
                System.out.println("New peer connected: " + socket.getRemoteSocketAddress());

                new Thread(() -> handlePeerCommunication(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToPeer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            connectedPeers.add(socket);
            System.out.println("Connected to peer: " + socket.getRemoteSocketAddress());

            new Thread(() -> handlePeerCommunication(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePeerCommunication(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String messageString;
            while ((messageString = reader.readLine()) != null) {
                System.out.println("Received from " + socket.getRemoteSocketAddress() + ": " + messageString);
                Message message = Message.fromJson(messageString);
                processMessage(message);

                // Example: Echo the message back to the sender
                writer.println(new Message("ECHO", new JSONObject().put("message", "Echo: " + messageString)).toJson());
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

    public void processMessage(Message message) {
        switch (message.getType()) {
            case "BLOCK":
                handleBlockMessage(message.getPayload());
                break;
            case "TRANSACTION":
                handleTransactionMessage(message.getPayload());
                break;
            case "PING":
                sendPongMessage();
                break;
            // Add more cases as needed
            default:
                System.out.println("Unknown message type: " + message.getType());
        }
    }

    private void handleBlockMessage(JSONObject payload) {
        System.out.println("Processing block: " + payload.toString());
        // Process the block here
    }

    private void handleTransactionMessage(JSONObject payload) {
        System.out.println("Processing transaction: " + payload.toString());
        // Process the transaction here
    }

    private void sendPongMessage() {
        JSONObject payload = new JSONObject().put("message", "PONG");
        broadcastMessage(new Message("PONG", payload).toJson());
    }

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
