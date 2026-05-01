package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import org.jetbrains.annotations.NotNull;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.*;

public class JsonHandler {
    private JsonNode root;

    public JsonHandler(@NotNull String jsonPathStr) {
        if (jsonPathStr.endsWith(".json")) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(jsonPathStr)) {
                setRoot(is);
            } catch (IOException e) {
                e.printStackTrace();
                App.popUpBackgroundError(e);
                System.exit(-1);
            }
        } else {
            throw new IllegalArgumentException(String.format("Invalid json path: %s", jsonPathStr));
        }
    }

    public void setRoot(InputStream is) {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            this.root = objMapper.readTree(is);
        } catch (Exception e) {
            e.printStackTrace();
            App.popUpBackgroundError(e);
            System.exit(-1);
        }
        // System.out.println("JSON loaded successfully from: " + JSON);
    }

    public JsonNode getRoot() {return this.root;}
}
