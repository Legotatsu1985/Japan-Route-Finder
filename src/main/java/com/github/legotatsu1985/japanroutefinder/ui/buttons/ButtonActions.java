package com.github.legotatsu1985.japanroutefinder.ui.buttons;

import com.github.legotatsu1985.japanroutefinder.ui.SettingsWindow;
import com.github.legotatsu1985.japanroutefinder.util.PropertyManager;

@Deprecated
public class ButtonActions {
    @Deprecated
    public static void exitApp() {
        System.exit(0);
    }
    @Deprecated
    public static void openSettings() {
        try {
            SettingsWindow settingsWindow = new SettingsWindow(null);
            settingsWindow.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error opening settings window: " + e.getMessage());
        }
    }
    @Deprecated
    public static void saveXlsxFilePath(String filePath, String xlsxPath, boolean checkboxState) {
        String xlsxFilePathKey = "xlsxFilePath";
        if (checkboxState) {
            if (xlsxPath != null && !xlsxPath.isEmpty()) {
                try {
                    PropertyManager.save(xlsxFilePathKey, xlsxPath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.err.println("XLSX file path is empty. Cannot save.");
            }
        } else {
            try {
                PropertyManager.remove(xlsxFilePathKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
