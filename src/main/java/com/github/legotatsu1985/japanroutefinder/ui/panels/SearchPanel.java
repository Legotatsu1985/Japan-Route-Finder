package com.github.legotatsu1985.japanroutefinder.ui.panels;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.JFrameBuildCheck;
import com.github.legotatsu1985.japanroutefinder.ui.MainWindow;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel implements JFrameBuildCheck {
    private JFrame parent;

    private BevelBorder fieldBorder;
    private JLabel originLabel, destinationLabel, searchStateLabel;
    private JTextField originField, destinationField;
    private JButton switchButton, resetButton, searchButton;

    public SearchPanel(JFrame parent) {
        super();
        this.parent = parent;
        // this.setMaximumSize(new Dimension(Short.MAX_VALUE, (Short.MAX_VALUE / 2)));
        this.setLayout(null);
        // this.setBackground(Color.PINK);
        this.initComponents();
        this.addAll();
        this.addListeners();
        setDevelopmentMode(App.CFG.isDevelopmentMode());
    }

    public void initComponents() {
        this.fieldBorder = new BevelBorder(BevelBorder.LOWERED);

        this.originLabel = new JLabel(App.LANG_HANDLER.getString("main_labelOrigin"));
        this.originLabel.setBounds(10, 10, 80, 20);
        this.destinationLabel = new JLabel(App.LANG_HANDLER.getString("main_labelDes"));
        this.destinationLabel.setBounds(10, 80, 80, 20);

        this.originField = new JTextField();
        this.originField.setBounds(100, 10, 100, 20);
        this.originField.setBorder(this.fieldBorder);
        this.destinationField = new JTextField();
        this.destinationField.setBounds(100, 80, 100, 20);
        this.destinationField.setBorder(this.fieldBorder);

        this.switchButton = new JButton(App.LANG_HANDLER.getString("main_switchRouteButton"));
        this.switchButton.setBounds(100, 40, 100, 30);
        this.resetButton = new JButton(App.LANG_HANDLER.getString("main_resetRouteButton"));
        this.resetButton.setBounds(10, 110, 90, 30);
        this.searchButton = new JButton(App.LANG_HANDLER.getString("main_searchRouteButton"));
        this.searchButton.setBounds(100, 110, 100, 30);
    }

    public void addAll() {
        this.add(this.originLabel);
        this.add(this.destinationLabel);
        this.add(this.originField);
        this.add(this.destinationField);
        this.add(this.switchButton);
        this.add(this.resetButton);
        this.add(this.searchButton);
    }

    public void addListeners() {
        this.originField.addActionListener(this);
        this.originField.setActionCommand("search");
        this.destinationField.addActionListener(this);
        this.destinationField.setActionCommand("search");
        this.switchButton.addActionListener(this);
        this.switchButton.setActionCommand("switch");
        this.resetButton.addActionListener(this);
        this.resetButton.setActionCommand("reset");
        this.searchButton.addActionListener(this);
        this.searchButton.setActionCommand("search");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "search":
                if (this.parent instanceof MainWindow) {
                    ((MainWindow) this.parent).searchRoute();
                } else {
                    App.UI.popupError(this.parent, "Error", "Parent frame must be an instance of \"MainWindow\"");
                }
                break;
            case "reset":
                if (this.originField.getText() != null || !this.originField.getText().isEmpty()) {
                    this.originField.setText("");
                }
                if (this.destinationField.getText() != null || !this.destinationField.getText().isEmpty()) {
                    this.destinationField.setText("");
                }
                break;
            case "switch":
                if (this.originField.getText() == null || this.destinationField.getText() == null || this.originField.getText().isEmpty() || this.destinationField.getText().isEmpty()) break;
                String origin = this.originField.getText();
                String destination = this.destinationField.getText();
                this.originField.setText(destination);
                this.destinationField.setText(origin);
                break;
            default: break;
        }
    }

    public void setDevelopmentMode(boolean b) {
        if (b) {
            this.setBorder(new LineBorder(Color.RED));
        } else {
            this.setBorder(null);
        }
        this.revalidate();
        this.repaint();
    }

    public String getOrigin() {return this.originField.getText();}

    public String getDestination() {return this.destinationField.getText();}
}
