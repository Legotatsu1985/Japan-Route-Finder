package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class UIHandler {
    private LookAndFeel laf;
    private MainWindow mainWindow;
    private SettingsWindow settingsWindow;

    public UIHandler() {
        this.laf = App.CFG.getWindowStyle();
        try {
            UIManager.setLookAndFeel(this.laf);
        } catch (UnsupportedLookAndFeelException e) {
            popupError(null, e);
        }
    }

    public void openMain() {
        this.mainWindow = new MainWindow();
        SwingUtilities.invokeLater(() -> this.mainWindow.setVisible(true));
    }

    public void openSettings() {
        this.settingsWindow = new SettingsWindow(this.mainWindow);
        SwingUtilities.invokeLater(() -> this.settingsWindow.setVisible(true));
    }

    public void updateLookAndFeel() {
        this.laf = App.CFG.getWindowStyle();
        try {
            UIManager.setLookAndFeel(this.laf);
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (UnsupportedLookAndFeelException e) {
            popupError(null, e);
        }
    }

    public void popupError(@Nullable Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void popupError(@Nullable Component parent, @NotNull Exception e) {
        JOptionPane.showMessageDialog(parent, e.getMessage(), "An error occurred!", JOptionPane.ERROR_MESSAGE);
    }
}
