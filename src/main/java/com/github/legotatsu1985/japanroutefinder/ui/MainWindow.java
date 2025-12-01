package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.components.FilesController;
import com.github.legotatsu1985.japanroutefinder.components.RouteFinder;
import com.github.legotatsu1985.japanroutefinder.ui.buttons.ButtonActions;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";

    private JPanel mainPanel;
    private Border border;
    private JLabel xlsxFileLabel;
    private JLabel originICAOLabel;
    private JLabel destICAOLabel;
    private JLabel resultRouteRemarksLabel;
    private JLabel resultRouteLabel;
    private JTextArea resultRouteRemarksTextArea;
    private JTextArea resultRouteTextArea;
    private JTextField xlsxFilePathField;
    private JTextField originICAOField;
    private JTextField destICAOField;
    private JCheckBox saveXlsxFilePathCheckBox;
    private JButton chooseXlsxButton;
    private JButton resetRouteButton;
    private JButton searchRouteButton;
    private JButton appSettingsButton;
    private JButton exitButton;

    public MainWindow() {
        this.setTitle(App.LANG.getText("main_windowTitle"));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(null);

        this.border = new BevelBorder(BevelBorder.LOWERED);

        this.xlsxFileLabel = new JLabel(App.LANG.getText("main_labelXlsxPath"));
        this.xlsxFileLabel.setBounds(10, 10, 100, 20);
        this.originICAOLabel = new JLabel(App.LANG.getText("main_labelOrigin"));
        this.originICAOLabel.setBounds(10, 40, 80, 20);
        this.destICAOLabel = new JLabel(App.LANG.getText("main_labelDes"));
        this.destICAOLabel.setBounds(10, 70, 80, 20);
        this.resultRouteRemarksLabel = new JLabel(App.LANG.getText("main_labelRouteRemark"));
        this.resultRouteRemarksLabel.setBounds(10, 140, 190, 20);
        this.resultRouteLabel = new JLabel(App.LANG.getText("main_labelRoute"));
        this.resultRouteLabel.setBounds(210, 140, 760, 20);

        this.resultRouteRemarksTextArea = new JTextArea();
        this.resultRouteRemarksTextArea.setBounds(10,160, 190, 280);
        this.resultRouteRemarksTextArea.setEditable(false);
        this.resultRouteRemarksTextArea.setBorder(this.border);
        this.resultRouteTextArea = new JTextArea();
        this.resultRouteTextArea.setBounds(210, 160, 760, 280);
        this.resultRouteTextArea.setEditable(false);
        this.resultRouteTextArea.setBorder(this.border);

        this.xlsxFilePathField = new JTextField();
        this.xlsxFilePathField.setBounds(100, 10, 300, 20);
        this.xlsxFilePathField.setBorder(this.border);
        this.originICAOField = new JTextField();
        this.originICAOField.setBounds(100, 40, 100, 20);
        this.originICAOField.setBorder(this.border);
        this.destICAOField = new JTextField();
        this.destICAOField.setBounds(100, 70, 100, 20);
        this.destICAOField.setBorder(this.border);

        this.saveXlsxFilePathCheckBox = new JCheckBox(App.LANG.getText("main_saveXlsxFilePath"));
        this.saveXlsxFilePathCheckBox.setBounds(520, 10, 150, 20);

        this.chooseXlsxButton = new JButton(App.LANG.getText("main_chooseXlsxButton"));
        this.chooseXlsxButton.setBounds(410, 10, 100, 20);
        this.resetRouteButton = new JButton(App.LANG.getText("main_resetRouteButton"));
        this.resetRouteButton.setBounds(10, 100, 90, 30);
        this.searchRouteButton = new JButton(App.LANG.getText("main_searchRouteButton"));
        this.searchRouteButton.setBounds(100, 100, 100, 30);
        this.appSettingsButton = new JButton(App.LANG.getText("main_labelSettings"));
        this.appSettingsButton.setBounds(500, 500, 100, 30);
        this.exitButton = new JButton(App.LANG.getText("main_exit"));
        this.exitButton.setBounds(400, 500, 100, 30);

        this.mainPanel.add(this.xlsxFileLabel);
        this.mainPanel.add(this.xlsxFilePathField);
        this.mainPanel.add(this.chooseXlsxButton);
        this.mainPanel.add(this.saveXlsxFilePathCheckBox);
        this.mainPanel.add(this.originICAOLabel);
        this.mainPanel.add(this.destICAOLabel);
        this.mainPanel.add(this.resultRouteRemarksLabel);
        this.mainPanel.add(this.resultRouteLabel);
        this.mainPanel.add(this.originICAOField);
        this.mainPanel.add(this.destICAOField);
        this.mainPanel.add(this.resetRouteButton);
        this.mainPanel.add(this.searchRouteButton);
        this.mainPanel.add(this.resultRouteRemarksTextArea);
        this.mainPanel.add(this.resultRouteTextArea);
        this.mainPanel.add(this.appSettingsButton);
        this.mainPanel.add(this.exitButton);

        this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);

        // Load saved XLSX file path if it exists
        if (FilesController.isPropertiesFileExists(APP_CONFIG_PROPERTIES_PATH)) {
            String savedXlsxFilePath = FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "xlsxFilePath");
            if (savedXlsxFilePath != null && !savedXlsxFilePath.isEmpty()) {
                this.xlsxFilePathField.setText(savedXlsxFilePath);
                this.saveXlsxFilePathCheckBox.setSelected(true);
            } else {
                this.saveXlsxFilePathCheckBox.setSelected(false);
            }
        }

        // Button actions
        this.chooseXlsxButton.addActionListener(_ -> {
            String xlsxFilePath = FilesController.chooseXlsxFile();
            if (xlsxFilePath != null) {
                this.xlsxFilePathField.setText(xlsxFilePath);
            }
        });
        this.saveXlsxFilePathCheckBox.addActionListener(_ -> {
            String xlsxFilePath = this.xlsxFilePathField.getText();
            boolean isChecked = this.saveXlsxFilePathCheckBox.isSelected();
            ButtonActions.saveXlsxFilePath(APP_CONFIG_PROPERTIES_PATH, xlsxFilePath, isChecked);
        });
        this.resetRouteButton.addActionListener(_ -> {
            //xlsxFilePathField.setText("");
            this.originICAOField.setText("");
            this.destICAOField.setText("");
            this.resultRouteTextArea.setText("");
        });
        this.searchRouteButton.addActionListener(_ -> {
            String xlsxFilePath = this.xlsxFilePathField.getText();
            String originICAO = this.originICAOField.getText().trim();
            String destICAO = this.destICAOField.getText().trim();

            if (xlsxFilePath.isEmpty() || originICAO.isEmpty() || destICAO.isEmpty()) {
                System.out.println("Please fill in all fields.");
            }

            String route;
            try {
                route = RouteFinder.findRoute(xlsxFilePath, originICAO, destICAO);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            if (route != null && !route.isEmpty()){
                this.resultRouteTextArea.setText(route);
            }
            String remarks;
            try {
                remarks = RouteFinder.findRouteRemarks(xlsxFilePath, originICAO, destICAO);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            if (remarks != null && !remarks.isEmpty()) {
                this.resultRouteRemarksTextArea.setText(remarks);
            }
        });
        this.exitButton.addActionListener(_ -> {
            String xlsxFilePath = this.xlsxFilePathField.getText();
            boolean isChecked = this.saveXlsxFilePathCheckBox.isSelected();
            ButtonActions.exitApp();
            ButtonActions.saveXlsxFilePath(APP_CONFIG_PROPERTIES_PATH, xlsxFilePath, isChecked);
        });
        this.appSettingsButton.addActionListener(_ -> ButtonActions.openSettings());
    }
}
