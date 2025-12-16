package com.github.legotatsu1985.japanroutefinder;

import com.github.legotatsu1985.japanroutefinder.ui.*;
import com.github.legotatsu1985.japanroutefinder.util.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    public static LangJsonLoader LANG;
    public static final FilesController FILES_CONTROLLER = new FilesController(APP_CONFIG_PROPERTIES_PATH);
    public static void main(String[] args) {
        if (!Files.exists(Paths.get(APP_CONFIG_PROPERTIES_PATH))) {
            System.out.println("Creating default properties file...");
            FILES_CONTROLLER.initProperties();
        }
        String langCode = LangJsonLoader.checkLang();
        LANG = new LangJsonLoader(langCode);
        // MainWindow mainWindow = new MainWindow();
        // mainWindow.setVisible(true);
        try {
            UIManager.setLookAndFeel(SettingsWindow.getWindowStyle());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
    public static void updateLookAndFeel() {
        try {
            UIManager.setLookAndFeel(SettingsWindow.getWindowStyle());
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
