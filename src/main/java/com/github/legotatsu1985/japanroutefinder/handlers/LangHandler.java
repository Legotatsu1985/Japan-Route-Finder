package com.github.legotatsu1985.japanroutefinder.handlers;

import org.jetbrains.annotations.NotNull;

public class LangHandler {
    private static final String BASE_PATH = "lang/";
    private JsonHandler jsonHandler;

    public LangHandler(@NotNull String langCode) {
        String langFilePath = String.format("%s%s.json", BASE_PATH, langCode);
        this.jsonHandler = new JsonHandler(langFilePath);
    }

    public String getString(@NotNull String key) {
        String value = String.format("[%s]", key);
        try {
            value = this.jsonHandler.getRoot().get(key).toString().trim();
        } catch (NullPointerException e) {
            System.err.printf("Key \"%s\" not found!%n", key);
        } finally {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
