package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.components.FilesController;
import com.github.legotatsu1985.japanroutefinder.components.RouteFinder;
import com.github.legotatsu1985.japanroutefinder.ui.buttons.ButtonActions;
import com.github.legotatsu1985.japanroutefinder.ui.textlabels.LangJsonLoader;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    public MainWindow() {
        String langCode = LangJsonLoader.checkLang();
        LangJsonLoader lang = new LangJsonLoader(langCode);

        setTitle(lang.getText("main_windowTitle"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        Border border = new BevelBorder(BevelBorder.LOWERED);

        JLabel xlsxFileLabel = new JLabel(lang.getText("main_labelXlsxPath"));
        xlsxFileLabel.setBounds(10, 10, 100, 20);
        JLabel originICAOLabel = new JLabel(lang.getText("main_labelOrigin"));
        originICAOLabel.setBounds(10, 40, 80, 20);
        JLabel destICAOLabel = new JLabel(lang.getText("main_labelDes"));
        destICAOLabel.setBounds(10, 70, 80, 20);
        JLabel resultRouteRemarksLabel = new JLabel(lang.getText("main_labelRouteRemark"));
        resultRouteRemarksLabel.setBounds(10, 140, 190, 20);
        JLabel resultRouteLabel = new JLabel(lang.getText("main_labelRoute"));
        resultRouteLabel.setBounds(210, 140, 760, 20);

        JTextArea resultRouteRemarksTextArea = new JTextArea();
        resultRouteRemarksTextArea.setBounds(10,160, 190, 280);
        resultRouteRemarksTextArea.setEditable(false);
        resultRouteRemarksTextArea.setBorder(border);
        JTextArea resultRouteTextArea = new JTextArea();
        resultRouteTextArea.setBounds(210, 160, 760, 280);
        resultRouteTextArea.setEditable(false);
        resultRouteTextArea.setBorder(border);

        JTextField xlsxFilePathField = new JTextField();
        xlsxFilePathField.setBounds(100, 10, 300, 20);
        xlsxFilePathField.setBorder(border);
        JTextField originICAOField = new JTextField();
        originICAOField.setBounds(100, 40, 100, 20);
        originICAOField.setBorder(border);
        JTextField destICAOField = new JTextField();
        destICAOField.setBounds(100, 70, 100, 20);
        destICAOField.setBorder(border);

        JCheckBox saveXlsxFilePathCheckBox = new JCheckBox(lang.getText("main_saveXlsxFilePath"));
        saveXlsxFilePathCheckBox.setBounds(520, 10, 150, 20);

        JButton chooseXlsxButton = new JButton(lang.getText("main_chooseXlsxButton"));
        chooseXlsxButton.setBounds(410, 10, 100, 20);
        JButton resetRouteButton = new JButton(lang.getText("main_resetRouteButton"));
        resetRouteButton.setBounds(10, 100, 90, 30);
        JButton searchRouteButton = new JButton(lang.getText("main_searchRouteButton"));
        searchRouteButton.setBounds(100, 100, 100, 30);
        JButton appSettingsButton = new JButton(lang.getText("main_labelSettings"));
        appSettingsButton.setBounds(500, 500, 100, 30);
        JButton exitButton = new JButton(lang.getText("main_exit"));
        exitButton.setBounds(400, 500, 100, 30);

        mainPanel.add(xlsxFileLabel);
        mainPanel.add(xlsxFilePathField);
        mainPanel.add(chooseXlsxButton);
        mainPanel.add(saveXlsxFilePathCheckBox);
        mainPanel.add(originICAOLabel);
        mainPanel.add(destICAOLabel);
        mainPanel.add(resultRouteRemarksLabel);
        mainPanel.add(resultRouteLabel);
        mainPanel.add(originICAOField);
        mainPanel.add(destICAOField);
        mainPanel.add(resetRouteButton);
        mainPanel.add(searchRouteButton);
        mainPanel.add(resultRouteRemarksTextArea);
        mainPanel.add(resultRouteTextArea);
        mainPanel.add(appSettingsButton);
        mainPanel.add(exitButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Load saved XLSX file path if it exists
        if (FilesController.isPropertiesFileExists(APP_CONFIG_PROPERTIES_PATH)) {
            String savedXlsxFilePath = FilesController.getProperty(APP_CONFIG_PROPERTIES_PATH, "xlsxFilePath");
            if (savedXlsxFilePath != null && !savedXlsxFilePath.isEmpty()) {
                xlsxFilePathField.setText(savedXlsxFilePath);
                saveXlsxFilePathCheckBox.setSelected(true);
            } else {
                saveXlsxFilePathCheckBox.setSelected(false);
            }
        }

        // Button actions
        chooseXlsxButton.addActionListener(e -> {
            String xlsxFilePath = FilesController.chooseXlsxFile();
            if (xlsxFilePath != null) {
                xlsxFilePathField.setText(xlsxFilePath);
            }
        });
        saveXlsxFilePathCheckBox.addActionListener(e -> {
            String xlsxFilePath = xlsxFilePathField.getText();
            boolean isChecked = saveXlsxFilePathCheckBox.isSelected();
            ButtonActions.saveXlsxFilePath(APP_CONFIG_PROPERTIES_PATH, xlsxFilePath, isChecked);
        });
        resetRouteButton.addActionListener(e -> {
            //xlsxFilePathField.setText("");
            originICAOField.setText("");
            destICAOField.setText("");
            resultRouteTextArea.setText("");
        });
        searchRouteButton.addActionListener(e -> {
            String xlsxFilePath = xlsxFilePathField.getText();
            String originICAO = originICAOField.getText().trim();
            String destICAO = destICAOField.getText().trim();

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
                resultRouteTextArea.setText(route);
            }
            String remarks;
            try {
                remarks = RouteFinder.findRouteRemarks(xlsxFilePath, originICAO, destICAO);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            if (remarks != null && !remarks.isEmpty()) {
                resultRouteRemarksTextArea.setText(remarks);
            }
        });
        exitButton.addActionListener(e -> {
            String xlsxFilePath = xlsxFilePathField.getText();
            boolean isChecked = saveXlsxFilePathCheckBox.isSelected();
            ButtonActions.exitApp();
            ButtonActions.saveXlsxFilePath(APP_CONFIG_PROPERTIES_PATH, xlsxFilePath, isChecked);
        });
        appSettingsButton.addActionListener(e -> ButtonActions.openSettings());
    }
}
