package com.github.legotatsu1985.japanroutefinder.util;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class PropertyManager {
    private static final Properties P = new Properties();
    private static final boolean IS_CFG_EXISTS = Files.exists(Config.CFG_PATH);

    public static void save(@NotNull String key, @NotNull String value) throws IOException {
        if (!IS_CFG_EXISTS) throw new FileNotFoundException("Properties file does not exist: " + App.APP_CONFIG_PROPERTIES_PATH);

        try (FileInputStream in = new FileInputStream(App.APP_CONFIG_PROPERTIES_PATH)) {
            P.load(in);
        } catch (IOException e) {
            throw new IOException("Error loading properties file: " + e.getMessage(), e);
        }
        P.setProperty(key, value);
        try (FileOutputStream out = new FileOutputStream(App.APP_CONFIG_PROPERTIES_PATH)) {
            P.store(out, null);
        } catch (IOException e) {
            throw new IOException("Error saving properties file: " + e.getMessage(), e);
        }
    }

    public static void save(@NotNull String key, int value) throws IOException {save(key, String.valueOf(value));}

    public static void remove(@NotNull String key) throws IOException {
        if (!IS_CFG_EXISTS) throw new FileNotFoundException("Properties file does not exist: " + App.APP_CONFIG_PROPERTIES_PATH);

        try (FileInputStream in = new FileInputStream(App.APP_CONFIG_PROPERTIES_PATH)) {
            P.load(in);
        } catch (IOException e) {
            throw new IOException("Error loading properties file: " + e.getMessage(), e);
        }
        P.remove(key);
        try (FileOutputStream out = new FileOutputStream(App.APP_CONFIG_PROPERTIES_PATH)) {
            P.store(out, null);
        } catch (IOException e) {
            throw new IOException("Error saving properties file: " + e.getMessage(), e);
        }
    }

    @Nullable
    public static String getString(@NotNull String key) throws IOException {
        if (!IS_CFG_EXISTS) throw new FileNotFoundException("Properties file does not exist: " + App.APP_CONFIG_PROPERTIES_PATH);

        try (FileInputStream in = new FileInputStream(App.APP_CONFIG_PROPERTIES_PATH)) {
            P.load(in);
            return P.getProperty(key);
        } catch (IOException e) {
            throw new IOException("Error loading properties file: " + e.getMessage(), e);
        }
    }

    public static int getInt(@NotNull String key) throws IOException, NumberFormatException {
        String value = getString(key);
        if (value == null) throw new IOException("Property not found: " + key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing integer property: " + e.getMessage(), e);
        }
    }
}
