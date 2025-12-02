package com.github.legotatsu1985.japanroutefinder.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

public class FilesController {
    public static boolean isPropertiesFileExists(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public static String getProperty(String filePath, String key) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public static void createPropertiesFile(String filePath) {
        Properties properties = new Properties();
        properties.setProperty("lang", "en"); // Default language
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Japan Route Finder Configuration");
        } catch (IOException e) {
            System.err.println("Error creating properties file: " + e.getMessage());
        }
    }
    public static void saveProperty(String filePath, String key, String value) {
        if (Files.exists(Paths.get(filePath))) {
            Properties properties = new Properties();
            try (FileInputStream input = new FileInputStream(filePath)) {
                properties.load(input);
            } catch (IOException e) {
                System.err.println("Error loading properties file: " + e.getMessage());
            }
            properties.setProperty(key, value);
            try (FileOutputStream output = new FileOutputStream(filePath)) {
                properties.store(output, "Updated property: " + key);
            } catch (IOException e) {
                System.err.println("Error saving property: " + e.getMessage());
            }
        } else {
            System.err.println("Properties file does not exist: " + filePath);
        }
    }
    public static void removeProperty(String filePath, String key) {
        if (Files.exists(Paths.get(filePath))) {
            Properties properties = new Properties();
            try (FileInputStream input = new FileInputStream(filePath)) {
                properties.load(input);
            } catch (IOException e) {
                System.err.println("Error loading properties file: " + e.getMessage());
            }
            properties.remove(key);
            try (FileOutputStream output = new FileOutputStream(filePath)) {
                properties.store(output, "Removed property: " + key);
            } catch (IOException e) {
                System.err.println("Error saving property: " + e.getMessage());
            }
        }
    }
    public static String chooseXlsxFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Excel File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx", "xls"));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null; // User cancelled the operation
        }
    }
}
