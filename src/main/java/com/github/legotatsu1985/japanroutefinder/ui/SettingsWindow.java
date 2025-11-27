package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.ui.buttons.ButtonActions;
import com.github.legotatsu1985.japanroutefinder.ui.textlabels.LangJsonLoader;
import com.github.legotatsu1985.japanroutefinder.components.FilesController;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    private final LangJsonLoader lang;
    public SettingsWindow() {
        String langCode = LangJsonLoader.checkLang();
        this.lang = new LangJsonLoader(langCode);

        this.setModal(true);
        this.setTitle(this.lang.getText("settings_windowTitle"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        JLabel langLabel = new JLabel();
        langLabel.setText(this.lang.getText("settings_labelLang"));
        langLabel.setBounds(10, 10, 200, 20);

        JButton saveButton = new JButton (this.lang.getText("settings_saveButton"));
        saveButton.setBounds(100, 225, 100, 30);
        JButton cancelButton = new JButton(this.lang.getText("settings_cancelButton"));
        cancelButton.setBounds(200, 225, 100, 30);

        JRadioButton langEnButton = new JRadioButton(this.lang.getText("settings_labelLangEn"));
        langEnButton.setBounds(10, 40, 200, 20);
        JRadioButton langJaButton = new JRadioButton(this.lang.getText("settings_labelLangJa"));
        langJaButton.setBounds(10, 70, 200, 20);
        
        String langTypeFromCfg = FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "lang");
        switch (langTypeFromCfg) {
            case "en":
                langEnButton.setSelected(true);
                break;
            case "ja":
                langJaButton.setSelected(true);
                break;
            case null:
                langEnButton.setEnabled(false);
                langJaButton.setEnabled(false);
                break;
            default:
                langEnButton.setSelected(true); // Default to English if no valid language is set
                break;
        }

        ButtonGroup langGroup = new ButtonGroup();
        langGroup.add(langEnButton);
        langGroup.add(langJaButton);

        mainPanel.add(langLabel);
        mainPanel.add(langEnButton);
        mainPanel.add(langJaButton);
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        saveButton.addActionListener(_ -> {
            String selectedLang = "";
            if (langEnButton.isSelected()) {
                selectedLang = "en";
                FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", "en");
            } else if (langJaButton.isSelected()) {
                selectedLang = "ja";
                FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", "ja");
            }
            if (langTypeFromCfg != null && !langTypeFromCfg.equals(selectedLang)) {
                JOptionPane.showMessageDialog(this, this.lang.getText("settings_langChangedRelaunchRequired"), this.lang.getText("settings_langChangedTitle"), JOptionPane.INFORMATION_MESSAGE);
                dispose();
                ButtonActions.exitApp();
            } else {
                dispose();
            }
        });

        cancelButton.addActionListener(_ -> dispose());
    }
}
