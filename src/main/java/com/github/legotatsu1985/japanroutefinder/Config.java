package com.github.legotatsu1985.japanroutefinder;

import com.github.legotatsu1985.japanroutefinder.util.PropertyManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public static Path CFG_PATH;

    // 設定項目
    private String langCode;
    private int styleInt;
    private LookAndFeel windowStyle;
    private @Nullable String xlsxFilePath;

    public Config(@NotNull String configFilePath) {
        CFG_PATH = Paths.get(configFilePath);
        if (!Files.exists(CFG_PATH)) createDefaultConfig();
        load();
    }

    private void createDefaultConfig() {
        try {
            Files.createFile(CFG_PATH);
            PropertyManager.save("lang", "en");
            PropertyManager.save("style", 0);
        } catch (Exception e) {
            throw new RuntimeException("Error creating default config: " + e.getMessage(), e);
        }
    }

    private void load() {
        try {
            this.langCode = PropertyManager.getString("lang");
            this.styleInt = PropertyManager.getInt("style");
            this.windowStyle = getLookAndFeel(this.styleInt);
            this.xlsxFilePath = PropertyManager.getString("xlsxFilePath");
        } catch (Exception e) {
            throw new RuntimeException("Error loading config: " + e.getMessage(), e);
        }
    }

    public void reload() {load();}

    @NotNull
    private LookAndFeel getLookAndFeel(int styleInt) {
        return switch (styleInt) {
            case 1 -> new com.formdev.flatlaf.FlatDarkLaf();
            case 2 -> new com.formdev.flatlaf.FlatIntelliJLaf();
            case 3 -> new com.formdev.flatlaf.FlatDarculaLaf();
            default -> new com.formdev.flatlaf.FlatLightLaf();
        };
    }

    public void saveXlsxFilePath(@NotNull String xlsxFilePath, boolean checkboxState) {
        String xlsxFilePathKey = "xlsxFilePath";
        if (checkboxState) {
            try {
                PropertyManager.save(xlsxFilePathKey, xlsxFilePath);
            } catch (IOException e) {
                throw new RuntimeException("Error saving xlsxFilePath: " + e.getMessage(), e);
            }
        } else {
            try {
                PropertyManager.remove(xlsxFilePathKey);
            } catch (IOException e) {
                throw new RuntimeException("Error saving xlsxFilePath: " + e.getMessage(), e);
            }
        }
    }

    public String getLangCode() {return langCode;}
    public int getStyleInt() {return styleInt;}
    public LookAndFeel getWindowStyle() {return windowStyle;}
    public @Nullable String getXlsxFilePath() {return xlsxFilePath;}
}
