package com.github.legotatsu1985.japanroutefinder;

import com.formdev.flatlaf.FlatLightLaf;

import com.github.legotatsu1985.japanroutefinder.ui.MainWindow;
import com.github.legotatsu1985.japanroutefinder.util.FilesController;
import com.github.legotatsu1985.japanroutefinder.util.LangJsonLoader;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    public static LangJsonLoader LANG;
    public static void main(String[] args) {
        if (!Files.exists(Paths.get(APP_CONFIG_PROPERTIES_PATH))) {
            System.out.println("Creating default properties file...");
            FilesController.createPropertiesFile(APP_CONFIG_PROPERTIES_PATH);
        }
        String langCode = LangJsonLoader.checkLang();
        LANG = new LangJsonLoader(langCode);
        // MainWindow mainWindow = new MainWindow();
        // mainWindow.setVisible(true);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
