package com.github.legotatsu1985.japanroutefinder.ui.textlabels;

import com.github.legotatsu1985.japanroutefinder.components.FilesController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.Map;
import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class LangJsonLoader {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    private static final String BASE_PATH = "lang/";
    private Map<String, String> messages;

    public LangJsonLoader(String langCode) {
        String filePath = BASE_PATH + langCode + ".json";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) throw new FileNotFoundException("Language file not found: " + filePath);
            String jsonText = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            messages = gson.fromJson(jsonText, new TypeToken<Map<String, String>>() {}.getType());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    public String getText(@NotNull String key) {
        return messages.getOrDefault(key, "[" + key + "]");
    }

    public static String checkLang() {
        String en = "en";
        String ja = "ja";
        if (FilesController.isPropertiesFileExists(APP_CONFIG_PROPERTIES_PATH)) {
            String lang = FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "lang");
            if ("ja".equals(lang)) {
                return ja;
            } else {
                return en;
            }
        } else {
            System.err.println("Properties file not found, defaulting to English.");
            return en;
        }
    }

    /* JSONファイルを読み込む古いコード

    public static String loadLangJson(@NotNull String key) throws Exception{
        String langFilePath = checkLang();
        if (langFilePath.equals(loadJson(LANG_JA_JSON_PATH))) {
            String langJson = Files.readString(Paths.get(loadJson(LANG_JA_JSON_PATH)), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(langJson);
            JsonNode valueNode = jsonNode.get(key);
            if (valueNode != null) {
                return valueNode.asText();
            } else {
                throw new Exception("Key not found in Japanese language file: " + key);
            }
        } else if (langFilePath.equals(loadJson(LANG_EN_JSON_PATH))) {
            String langJson = Files.readString(Paths.get(loadJson(LANG_EN_JSON_PATH)), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(langJson);
            JsonNode valueNode = jsonNode.get(key);
            if (valueNode != null) {
                return valueNode.asText();
            } else {
                throw new Exception("Key not found in English language file: " + key);
            }
        } else {
            throw new Exception("Invalid language file path: " + langFilePath);
        }
    }
    */
}
