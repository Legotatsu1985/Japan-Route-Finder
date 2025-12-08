package com.github.legotatsu1985.japanroutefinder.ui.buttons;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.SettingsWindow;
import com.github.legotatsu1985.japanroutefinder.util.FilesController;

public class ButtonActions {
    public static void exitApp() {
        System.exit(0);
    }
    public static void openSettings() {
        try {
            SettingsWindow settingsWindow = new SettingsWindow();
            settingsWindow.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error opening settings window: " + e.getMessage());
        }
    }
    public static void saveXlsxFilePath(String filePath, String xlsxPath, boolean checkboxState) {
        String xlsxFilePathKey = "xlsxFilePath";
        if (checkboxState) {
            if (xlsxPath != null && !xlsxPath.isEmpty()) {
                try {
                    App.FILES_CONTROLLER.saveProperty(xlsxFilePathKey, xlsxPath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.err.println("XLSX file path is empty. Cannot save.");
            }
        } else {
            try {
                App.FILES_CONTROLLER.removeProperty(xlsxFilePathKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
