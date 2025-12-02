package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.util.FilesController;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    private static final String[] LANGUAGES = {"English", "Japanese"};
    private static final String[] LANG_CODES = {"en", "ja"};
    private static final String[] WINDOW_STYLES = {"Light", "Dark", "IntelliJ", "Darcula"};

    private JPanel mainPanel;
    private JLabel langLabel;
    private JLabel styleLabel;
    private JButton saveButton;
    private JButton cancelButton;
    // private JRadioButton langEnButton;
    // private JRadioButton langJaButton;
    // private ButtonGroup langGroup;
    private JComboBox<String> langComboBox;
    private JComboBox<String> styleComboBox;

    public SettingsWindow() {
        super((JFrame) null, App.LANG.getText("settings_windowTitle"), true);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        initComponents();
    }
    private void initComponents() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);

        this.langLabel = new JLabel();
        this.langLabel.setText(App.LANG.getText("settings_labelLang"));
        this.langLabel.setBounds(10, 10, 200, 20);
        this.styleLabel = new JLabel();
        this.styleLabel.setText(App.LANG.getText("settings_labelStyle"));
        this.styleLabel.setBounds(10, 40, 200, 20);

        this.saveButton = new JButton (App.LANG.getText("settings_saveButton"));
        this.saveButton.setBounds(100, 225, 100, 30);
        this.cancelButton = new JButton(App.LANG.getText("settings_cancelButton"));
        this.cancelButton.setBounds(200, 225, 100, 30);

        this.langComboBox = new JComboBox<>(LANGUAGES);
        this.langComboBox.setBounds(220, 10, 150, 20);
        this.langComboBox.setSelectedIndex(getLangIndex(FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "lang")));
        this.styleComboBox = new JComboBox<>(WINDOW_STYLES);
        this.styleComboBox.setBounds(220, 40, 150, 20);
        this.styleComboBox.setEnabled(false); // Disabled until saving mechanism is implemented.

        /*
        this.langEnButton = new JRadioButton(App.LANG.getText("settings_labelLangEn"));
        this.langEnButton.setBounds(10, 40, 200, 20);
        this.langJaButton = new JRadioButton(App.LANG.getText("settings_labelLangJa"));
        this.langJaButton.setBounds(10, 70, 200, 20);


        String langTypeFromCfg = FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "lang");
        switch (langTypeFromCfg) {
            case "en":
                this.langEnButton.setSelected(true);
                break;
            case "ja":
                this.langJaButton.setSelected(true);
                break;
            case null:
                this.langEnButton.setEnabled(false);
                this.langJaButton.setEnabled(false);
                break;
            default:
                this.langEnButton.setSelected(true); // Default to English if no valid language is set
                break;
        }


        this.langGroup = new ButtonGroup();
        this.langGroup.add(this.langEnButton);
        this.langGroup.add(this.langJaButton);
         */

        this.mainPanel.add(this.langLabel);
        this.mainPanel.add(this.styleLabel);
        // this.mainPanel.add(this.langEnButton);
        // this.mainPanel.add(this.langJaButton);
        this.mainPanel.add(this.langComboBox);
        this.mainPanel.add(this.styleComboBox);
        this.mainPanel.add(this.saveButton);
        this.mainPanel.add(this.cancelButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        this.saveButton.addActionListener(_ -> {
            String selectedLangCode = LANG_CODES[this.langComboBox.getSelectedIndex()];
            FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", selectedLangCode);
            /*
            String selectedLang = "";
            if (this.langEnButton.isSelected()) {
                selectedLang = "en";
                FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", "en");
            } else if (this.langJaButton.isSelected()) {
                selectedLang = "ja";
                FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", "ja");
            }
            if (langTypeFromCfg != null && !langTypeFromCfg.equals(selectedLang)) {
                JOptionPane.showMessageDialog(this, App.LANG.getText("settings_langChangedRelaunchRequired"), App.LANG.getText("settings_langChangedTitle"), JOptionPane.INFORMATION_MESSAGE);
                dispose();
                ButtonActions.exitApp();
            } else {
                dispose();
            }
             */
            dispose();
        });
        this.cancelButton.addActionListener(_ -> dispose());
    }
    private static int getLangIndex(String langCode) {
        for (int i = 0; i < LANG_CODES.length; i++) {
            if (LANG_CODES[i].equalsIgnoreCase(langCode)) {
                return i;
            }
        }
        return 0; // Default to English if not found
    }
}
