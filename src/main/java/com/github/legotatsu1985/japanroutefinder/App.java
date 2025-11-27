package com.github.legotatsu1985.japanroutefinder;

import com.formdev.flatlaf.*;

import com.github.legotatsu1985.japanroutefinder.ui.MainWindow;
import com.github.legotatsu1985.japanroutefinder.components.FilesController;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    public static void main(String[] args) throws Exception {
        if (!Files.exists(Paths.get(APP_CONFIG_PROPERTIES_PATH))) {
            System.out.println("Creating default properties file...");
            FilesController.createPropertiesFile(APP_CONFIG_PROPERTIES_PATH);
        }
        // MainWindow mainWindow = new MainWindow();
        // mainWindow.setVisible(true);
        UIManager.setLookAndFeel(new FlatLightLaf());
        SwingUtilities.invokeLater(() -> {
            try {
                new MainWindow().setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
