package com.github.legotatsu1985.japanroutefinder.handlers;

import org.jetbrains.annotations.NotNull;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.*;

public class JsonHandler {
    private static String JSON;
    private static File JSON_FILE;
    private JsonNode root;

    public JsonHandler(@NotNull String jsonPathStr) {
        if (jsonPathStr.endsWith(".json")) {
            /*try (InputStream in = getClass().getClassLoader().getResourceAsStream(jsonPathStr)) {
                if (in == null) throw new FileNotFoundException("JSON file not found: " + jsonPathStr);
                JSON = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            JSON_FILE = new File(jsonPathStr);
            setRoot();
        } else {
            throw new IllegalArgumentException(String.format("Invalid json path: %s", jsonPathStr));
        }
    }

    private void setRoot() {
        ObjectMapper objMapper = new ObjectMapper();
        this.root = objMapper.readTree(JSON_FILE);
        // System.out.println("JSON loaded successfully from: " + JSON);
    }

    public JsonNode getRoot() {return this.root;}
}
