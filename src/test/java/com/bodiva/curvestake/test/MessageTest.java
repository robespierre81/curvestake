package com.bodiva.curvestake.test;

import com.bodiva.curvestake.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageCreation() {
        String type = "BLOCK";
        JSONObject payload = new JSONObject();
        payload.put("hash", "abc123");
        payload.put("previousHash", "def456");
        payload.put("data", "This is some block data");

        // Create a Message object
        Message message = new Message(type, payload);

        // Verify the type and payload
        assertEquals(type, message.getType(), "Message type should be 'BLOCK'");
        assertEquals(payload.toString(), message.getPayload().toString(), "Message payload should match the provided JSON object");
    }

    @Test
    public void testMessageToJson() {
        String type = "TRANSACTION";
        JSONObject payload = new JSONObject();
        payload.put("transactionId", "tx123");
        payload.put("amount", 1000);

        // Create a Message object
        Message message = new Message(type, payload);

        // Convert the Message object to a JSON string
        String jsonString = message.toJson();

        // Expected JSON string format
        String expectedJsonString = new JSONObject()
            .put("type", type)
            .put("payload", payload)
            .toString();

        // Verify that the JSON string matches the expected format
        assertEquals(expectedJsonString, jsonString, "JSON string should match the expected format");
    }

    @Test
    public void testMessageFromJson() {
        // JSON string to parse
        String jsonString = "{\"type\":\"PEER\",\"payload\":{\"ip\":\"192.168.1.1\",\"port\":8080}}";

        // Parse the JSON string into a Message object
        Message message = Message.fromJson(jsonString);

        // Verify the type and payload
        assertEquals("PEER", message.getType(), "Message type should be 'PEER'");
        assertEquals("192.168.1.1", message.getPayload().getString("ip"), "Payload IP should be '192.168.1.1'");
        assertEquals(8080, message.getPayload().getInt("port"), "Payload port should be 8080");
    }

    @Test
    public void testMessageEquality() {
        String type = "BLOCK";
        JSONObject payload = new JSONObject();
        payload.put("hash", "abc123");
        payload.put("previousHash", "def456");
        payload.put("data", "This is some block data");

        // Create two identical Message objects
        Message message1 = new Message(type, payload);
        Message message2 = new Message(type, payload);

        // Verify that their JSON representations are equal
        assertEquals(message1.toJson(), message2.toJson(), "Two identical Message objects should have the same JSON representation");

        // Parse the JSON string back into a Message object and check equality
        Message parsedMessage1 = Message.fromJson(message1.toJson());
        Message parsedMessage2 = Message.fromJson(message2.toJson());

        assertEquals(parsedMessage1.toJson(), parsedMessage2.toJson(), "Parsed Message objects from identical JSON strings should be equal");
    }
}
