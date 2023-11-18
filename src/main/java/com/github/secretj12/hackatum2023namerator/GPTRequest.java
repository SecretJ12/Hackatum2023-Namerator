package com.github.secretj12.hackatum2023namerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GPTRequest {
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;
    private GPTModels model;
    private String system_message;
    private String user_message;

    public JSONObject buildRequest() throws JSONException {
        JSONObject system = new JSONObject();
        system.put("role", "system");
        system.put("content", system_message);
        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", user_message);

        JSONObject data = new JSONObject();
        data.put("temperature", temperature);
        data.put("max_tokens", max_tokens);
        data.put("top_p", top_p);
        data.put("frequency_penalty", frequency_penalty);
        data.put("presence_penalty", presence_penalty);
        data.put("model", model == GPTModels.GPT4 ? "gpt-4" : "gpt-3.5-turbo");
        data.put("messages", new JSONArray(List.of(system, user)));

        return data;
    }

    @Override
    public String toString() {
        try {
            return buildRequest().toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public GPTRequest(double temperature, int max_tokens, double top_p, double frequency_penalty, double presence_penalty, GPTModels model, String system_message, String user_message) {
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
        this.frequency_penalty = frequency_penalty;
        this.presence_penalty = presence_penalty;
        this.model = model;
        this.system_message = system_message;
        this.user_message = user_message;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public void setTop_p(double top_p) {
        this.top_p = top_p;
    }

    public void setFrequency_penalty(double frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public void setPresence_penalty(double presence_penalty) {
        this.presence_penalty = presence_penalty;
    }

    public void setModel(GPTModels model) {
        this.model = model;
    }

    public void setSystem_message(String system_message) {
        this.system_message = system_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }
}
