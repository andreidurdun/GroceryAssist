package com.grocery.assist.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BertClassifier {
    public BertClassifier() {}

    public String predict(String inputText) throws IOException {
        URL url = new URL("http://localhost:8000/predict");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Configurarea cererii POST
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json"); // Set content type to JSON
        con.setDoOutput(true); // indică faptul că vom trimite date în corpul cererii.

        String jsonInputString = "{\"text\": \"" + inputText + "\"}";

        //Trimitem cererea
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        // Citim răspunsul
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Parsam JSON-ul și extragem doar valoarea
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("label");
        }
    }

    public List<String> predictBulk(List<String> texts) throws IOException {
        URL url = new URL("http://localhost:8000/predict_bulk");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Construim JSON: {"texts": ["lapte", "hartie igienica", "paine"]}
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("texts", new JSONArray(texts));

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInput.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Citim răspunsul
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // {"labels": ["produse lactate", "igienă", "panificație"]}
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray labels = jsonResponse.getJSONArray("labels");

            return labels.toList().stream().map(Object::toString).toList();
        }
    }

    public static void main(String[] args) throws IOException {
        BertClassifier bertClassifier = new BertClassifier();
        List<String> inputText = new ArrayList<String>();
        inputText = List.of("lapte", "hartie igienica", "paine");
        List<String> predictions = bertClassifier.predictBulk(inputText);
        System.out.println(predictions.toString());
    }
}
