package com.github.secretj12.hackatum2023namerator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPTRequester {
    private static String key;

    public static void setKey(String key) {
        GPTRequester.key = key;
    }

    public static String sendRequest(GPTRequest request) throws Exception {
        if (key == null || key.isEmpty()) {
            System.err.println("No key specified");
            return "";
        }

        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + key);

        System.out.println(request.toString());
        con.setDoOutput(true);
        con.getOutputStream().write(request.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
