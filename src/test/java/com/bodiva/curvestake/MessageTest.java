package com.bodiva.curvestake;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageSerialization() {
        // Create a sample payload
        JSONObject payload = new JSONObject();
        payload.put("hash", "abc123...");
        payload.put("previousHash", "def456...");
        payload.put("data", "Some transaction data");
        payload.put("timestamp", 1234567890);
        payload.put("signature", "base64-encoded-signature");

        // Create a message with type "BLOCK"
        Message message = new Message("BLOCK", payload);

        // Convert the message to JSON string
        String jsonString = message.toJson();

        // Expected JSON string format
        String expectedJsonString = "{\"type\":\"BLOCK\",\"payload\":{\"hash\":\"abc123...\",\"previousHash\":\"def456...\",\"data\":\"Some transaction data\",\"timestamp\":1234567890,\"signature\":\"base64-encoded-signature\"}}";

        // Check if the serialized JSON matches the expected output
        assertEquals(expectedJsonString, jsonString);
    }

    @Test
    public void testMessageDeserialization() {
        // JSON string to deserialize
        String jsonString = "{\"type\":\"BLOCK\",\"payload\":{\"hash\":\"abc123...\",\"previousHash\":\"def456...\",\"data\":\"Some transaction data\",\"timestamp\":1234567890,\"signature\":\"base64-encoded-signature\"}}";

        // Convert JSON string back to a Message object
        Message message = Message.fromJson(jsonString);

        // Check the type and payload
        assertEquals("BLOCK", message.getType());
        JSONObject payload = message.getPayload();
        assertEquals("abc123...", payload.getString("hash"));
        assertEquals("def456...", payload.getString("previousHash"));
        assertEquals("Some transaction data", payload.getString("data"));
        assertEquals(1234567890, payload.getLong("timestamp"));
        assertEquals("base64-encoded-signature", payload.getString("signature"));
    }

    @Test
    public void testMessageEquality() {
        // Create two identical messages
        JSONObject payload = new JSONObject();
        payload.put("hash", "abc123...");
        payload.put("previousHash", "def456...");
        payload.put("data", "Some transaction data");
        payload.put("timestamp", 1234567890);
        payload.put("signature", "base64-encoded-signature");

        Message message1 = new Message("BLOCK", payload);
        Message message2 = new Message("BLOCK", payload);

        // Ensure that their JSON strings are equal
        assertEquals(message1.toJson(), message2.toJson());

        // Ensure that deserialized messages are also equal
        Message deserializedMessage1 = Message.fromJson(message1.toJson());
        Message deserializedMessage2 = Message.fromJson(message2.toJson());
        assertEquals(deserializedMessage1.toJson(), deserializedMessage2.toJson());
    }
}
