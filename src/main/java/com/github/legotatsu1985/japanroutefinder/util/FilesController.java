package com.github.legotatsu1985.japanroutefinder.util;

import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

public class FilesController {
    private Properties properties;
    private String filePathString;
    private Path filePath;
    public FilesController(String filePath) {
        this.properties = new Properties();
        this.filePathString = filePath;
        this.filePath = Paths.get(filePath);
    }
    public void initProperties() {
        if (this.properties != null && this.filePathString != null) {
            this.properties.setProperty("lang", "en");
            try (FileOutputStream output = new FileOutputStream(this.filePathString)) {
                this.properties.store(output, "Japan Route Finder Configuration");
            } catch (IOException e) {
                System.err.println("Error creating properties file: " + e.getMessage());
            }
        } else {
            throw new IllegalStateException("Properties object or file path is null.");
        }
    }
    public boolean isFileExists() {
        if (this.properties != null && this.filePathString != null) {
            try (FileInputStream input = new FileInputStream(this.filePathString)) {
                this.properties.load(input);
                return true;
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            }
        } else {
            throw new IllegalStateException("Properties object or file path is null.");
        }
    }
    @Nullable
    public String getProperty(String key) {
        if (this.properties != null && this.filePathString != null) {
            try (FileInputStream input = new FileInputStream(this.filePathString)) {
                this.properties.load(input);
                return properties.getProperty(key);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return null;
            }
        } else {
            throw new IllegalStateException("Properties object or file path is null.");
        }
    }
    public void saveProperty(String key, String value) throws Exception {
        if (this.properties != null && this.filePathString != null) {
            if (Files.exists(this.filePath)) {
                try (FileInputStream input = new FileInputStream(this.filePathString)) {
                    this.properties.load(input);
                } catch (IOException e) {
                    System.err.println("Error loading properties file: " + e.getMessage());
                }
                this.properties.setProperty(key, value);
                try (FileOutputStream output = new FileOutputStream(this.filePathString)) {
                    this.properties.store(output, "Updated property: " + key);
                } catch (IOException e) {
                    System.err.println("Error saving property: " + e.getMessage());
                }
            } else {
                throw new FileNotFoundException("Properties file does not exist: " + this.filePathString);
            }
        } else {
            throw new IllegalStateException("Properties object or file path is null.");
        }
    }
    public void removeProperty(String key) throws Exception {
        if (this.properties != null && this.filePathString != null) {
            if (Files.exists(filePath)) {
                try (FileInputStream input = new FileInputStream(this.filePathString)) {
                    this.properties.load(input);
                } catch (IOException e) {
                    System.err.println("Error loading properties file: " + e.getMessage());
                }
                this.properties.remove(key);
                try (FileOutputStream output = new FileOutputStream(this.filePathString)) {
                    this.properties.store(output, "Removed property: " + key);
                } catch (IOException e) {
                    System.err.println("Error saving property: " + e.getMessage());
                }
            } else {
                throw new FileNotFoundException("Properties file does not exist: " + this.filePathString);
            }
        } else {
            throw new IllegalStateException("Properties object or file path is null.");
        }
    }
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public static void createPropertiesFile(String filePath) {
        Properties properties = new Properties();
        properties.setProperty("lang", "en"); // Default language
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Japan Route Finder Configuration");
        } catch (IOException e) {
            System.err.println("Error creating properties file: " + e.getMessage());
        }
    }
    @Deprecated
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
    @Deprecated
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
