package com.bodiva.curvestake;

import org.json.JSONObject;

public class Message {
    private String type;
    private JSONObject payload;

    public Message(String type, JSONObject payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public JSONObject getPayload() {
        return payload;
    }

    // Convert Message to JSON string
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("payload", payload);
        return json.toString();
    }

    // Parse JSON string to Message object
    public static Message fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String type = json.getString("type");
        JSONObject payload = json.getJSONObject("payload");
        return new Message(type, payload);
    }
}
