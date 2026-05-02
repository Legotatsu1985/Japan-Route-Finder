package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.App;
import com.formdev.flatlaf.*;
import com.github.legotatsu1985.japanroutefinder.util.PropertyManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsWindow extends JDialog implements JFrameBuildCheck {
    private static final String[] LANGUAGES = {"English", "日本語"};
    private static final String[] LANG_CODES = {"en", "ja"};
    private static final String[] WINDOW_STYLES = {"Light", "Dark", "IntelliJ", "Darcula"};

    private JPanel mainPanel;
    private JLabel langLabel, styleLabel, excelPathLabel, developmentModeLabel;
    private JButton saveButton, cancelButton, selectExcelPathButton;
    private JTextField excelPathField;
    private JComboBox<String> langComboBox, styleComboBox;
    private JCheckBox developmentModeCheckBox;

    public SettingsWindow(Frame parent) {
        super(parent, App.LANG_HANDLER.getString("settings_windowTitle"), true);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        initComponents();
        addAll();
        addListeners();
        setDevelopmentMode(App.CFG.isDevelopmentMode());
    }
    public void initComponents() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);

        this.langLabel = new JLabel();
        this.langLabel.setText(App.LANG_HANDLER.getString("settings_labelLang"));
        this.langLabel.setBounds(10, 10, 200, 20);
        this.styleLabel = new JLabel();
        this.styleLabel.setText(App.LANG_HANDLER.getString("settings_labelStyle"));
        this.styleLabel.setBounds(10, 40, 200, 20);
        this.excelPathLabel = new JLabel();
        this.excelPathLabel.setText(App.LANG_HANDLER.getString("settings_labelExcelPath"));
        this.excelPathLabel.setBounds(10, 70, 200, 20);
        this.developmentModeLabel = new JLabel();
        this.developmentModeLabel.setText(App.LANG_HANDLER.getString("settings_labelDevelopmentMode"));
        this.developmentModeLabel.setBounds(10, 100, 200, 20);

        this.saveButton = new JButton (App.LANG_HANDLER.getString("settings_saveButton"));
        this.saveButton.setBounds(100, 225, 100, 30);
        this.cancelButton = new JButton(App.LANG_HANDLER.getString("settings_cancelButton"));
        this.cancelButton.setBounds(200, 225, 100, 30);
        this.selectExcelPathButton = new JButton (App.LANG_HANDLER.getString("settings_labelSelectExcelPathButton"));
        this.selectExcelPathButton.setBounds(350, 70, 20, 20);

        this.langComboBox = new JComboBox<>(LANGUAGES);
        this.langComboBox.setBounds(220, 10, 150, 20);
        this.langComboBox.setSelectedIndex(getLangIndex(App.CFG.getLangCode()));
        this.styleComboBox = new JComboBox<>(WINDOW_STYLES);
        this.styleComboBox.setBounds(220, 40, 150, 20);
        this.styleComboBox.setSelectedIndex(App.CFG.getStyleInt());

        this.excelPathField = new JTextField();
        this.excelPathField.setBounds(220, 70, 130, 20);
        this.excelPathField.setText(App.CFG.getXlsxFilePath());

        this.developmentModeCheckBox = new JCheckBox();
        this.developmentModeCheckBox.setBounds(220, 100, 130, 20);
        this.developmentModeCheckBox.setSelected(App.CFG.isDevelopmentMode());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void addAll() {
        this.mainPanel.add(this.langLabel);
        this.mainPanel.add(this.styleLabel);
        this.mainPanel.add(this.excelPathLabel);
        this.mainPanel.add(this.developmentModeLabel);
        this.mainPanel.add(this.langComboBox);
        this.mainPanel.add(this.styleComboBox);
        this.mainPanel.add(this.excelPathField);
        this.mainPanel.add(this.saveButton);
        this.mainPanel.add(this.cancelButton);
        this.mainPanel.add(this.selectExcelPathButton);
        this.mainPanel.add(this.developmentModeCheckBox);
    }

    public void addListeners() {
        this.saveButton.addActionListener(this);
        this.saveButton.setActionCommand("save");
        this.cancelButton.addActionListener(this);
        this.cancelButton.setActionCommand("cancel");
        this.selectExcelPathButton.addActionListener(this);
        this.selectExcelPathButton.setActionCommand("selectExcelPath");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "save":
                saveAll();
                App.CFG.reload();
                App.UI.updateLookAndFeel();
                App.UI.updateDevelopmentMode();
                dispose();
            case "cancel": dispose(); break;
            case "selectExcelPath":
                String selectedExcelPath = App.UI.chooseFile(this, "Select Excel File", "Excel File", "xlsx", "xls");
                if (selectedExcelPath == null) return;
                this.excelPathField.setText(selectedExcelPath);
                break;
            default: break;
        }
    }
    public void setDevelopmentMode(boolean b) {}

    private void saveAll() {
        String selectedLangCode = LANG_CODES[this.langComboBox.getSelectedIndex()];
        int selectedStyleIndex = this.styleComboBox.getSelectedIndex();
        int devMode = this.developmentModeCheckBox.isSelected() ? 1 : 0;
        try {
            PropertyManager.save("lang", selectedLangCode);
            PropertyManager.save("style", selectedStyleIndex);
            PropertyManager.save("devMode", devMode);
            PropertyManager.save("xlsxFilePath", this.excelPathField.getText() != null ? this.excelPathField.getText() : "");
        } catch (Exception e) {App.UI.popupError(this, e);}
    }

    private static int getLangIndex(String langCode) {
        for (int i = 0; i < LANG_CODES.length; i++) {
            if (LANG_CODES[i].equalsIgnoreCase(langCode)) {
                return i;
            }
        }
        return 0; // Default to English if not found
    }

    @Deprecated
    @NotNull
    public static LookAndFeel getWindowStyle() {
        int styleIndex = App.CFG.getStyleInt();
        switch (styleIndex) {
            case 1 -> {return new FlatDarkLaf();}
            case 2 -> {return new FlatIntelliJLaf();}
            case 3 -> {return new FlatDarculaLaf();}
            default -> {return new FlatLightLaf();}
        }
    }
}
