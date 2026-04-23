package com.github.legotatsu1985.japanroutefinder.handlers;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class UIHandler {
    private LookAndFeel laf;

    public UIHandler() {
        this.laf = App.CFG.getWindowStyle();
        try {
            UIManager.setLookAndFeel(this.laf);
        } catch (UnsupportedLookAndFeelException e) {
            popupError(null, e);
        }
    }

    public void openMain() {SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));}

    public void openSettings() {SwingUtilities.invokeLater(() -> new SettingsWindow().setVisible(true));}

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
