package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.ui.buttons.ButtonActions;
import com.github.legotatsu1985.japanroutefinder.ui.textlabels.LangJsonLoader;
import com.github.legotatsu1985.japanroutefinder.components.FilesController;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    private final LangJsonLoader lang;

    private JPanel mainPanel;
    private JLabel langLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JRadioButton langEnButton;
    private JRadioButton langJaButton;
    private ButtonGroup langGroup;

    public SettingsWindow() {
        String langCode = LangJsonLoader.checkLang();
        this.lang = new LangJsonLoader(langCode);

        this.setModal(true);
        this.setTitle(this.lang.getText("settings_windowTitle"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        initComponents();
    }
    private void initComponents() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);

        this.langLabel = new JLabel();
        this.langLabel.setText(this.lang.getText("settings_labelLang"));
        this.langLabel.setBounds(10, 10, 200, 20);

        this.saveButton = new JButton (this.lang.getText("settings_saveButton"));
        this.saveButton.setBounds(100, 225, 100, 30);
        this.cancelButton = new JButton(this.lang.getText("settings_cancelButton"));
        this.cancelButton.setBounds(200, 225, 100, 30);

        this.langEnButton = new JRadioButton(this.lang.getText("settings_labelLangEn"));
        this.langEnButton.setBounds(10, 40, 200, 20);
        this.langJaButton = new JRadioButton(this.lang.getText("settings_labelLangJa"));
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

        this.mainPanel.add(this.langLabel);
        this.mainPanel.add(this.langEnButton);
        this.mainPanel.add(this.langJaButton);
        this.mainPanel.add(this.saveButton);
        this.mainPanel.add(this.cancelButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        this.saveButton.addActionListener(_ -> {
            String selectedLang = "";
            if (this.langEnButton.isSelected()) {
                selectedLang = "en";
                FilesController.saveProperty(APP_CONFIG_PROPERTIES_PATH, "lang", "en");
            } else if (this.langJaButton.isSelected()) {
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
        this.cancelButton.addActionListener(_ -> dispose());
    }
}
