package com.github.legotatsu1985.japanroutefinder.ui.panels;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.IJFrame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InternationalSearchPanel extends JPanel implements IJFrame {
    public static final int OUTBOUND = 0;
    public static final int INBOUND = 1;

    private final JFrame parent;
    private final int direction;

    private BevelBorder fieldBorder;
    private JLabel label1, label2, directionLabel;
    private JTextField field1, field2;
    private JButton resetButton, searchButton;

    public InternationalSearchPanel(JFrame parent, int direction) {
        super();
        this.parent = parent;
        this.direction = direction;
        this.setLayout(null);
        this.initComponents();
        this.addAll();
        this.addListeners();
        this.setDevelopmentMode(App.CFG.isDevelopmentMode());
    }

    public void initComponents() {
        this.fieldBorder = new BevelBorder(BevelBorder.LOWERED);

        if (this.direction == OUTBOUND) {
            this.label1 = new JLabel(App.LANG_HANDLER.getString("main_labelOrigin"));
            this.label1.setBounds(10, 10, 80, 20);
            this.label2 = new JLabel(App.LANG_HANDLER.getString("main_labelExitWaypoint"));
            this.label2.setBounds(10, 80, 80, 20);
        } else if (this.direction == INBOUND) {
            this.label1 = new JLabel(App.LANG_HANDLER.getString("main_labelEntryWaypoint"));
            this.label1.setBounds(10, 10, 80, 20);
            this.label2 = new JLabel(App.LANG_HANDLER.getString("main_labelDes"));
            this.label2.setBounds(10, 80, 80, 20);
        } else {
            throw new IllegalArgumentException("Invalid direction: " + this.direction);
        }
        this.field1 = new JTextField();
        this.field1.setBounds(100, 10, 100, 20);
        this.field1.setBorder(this.fieldBorder);
        this.field2 = new JTextField();
        this.field2.setBounds(100, 80, 100, 20);
        this.field2.setBorder(this.fieldBorder);
        this.directionLabel = new JLabel("↓↓");
        this.directionLabel.setBounds(100, 40, 100, 30);
        this.directionLabel.setHorizontalAlignment(JLabel.CENTER);
        this.directionLabel.setVerticalAlignment(JLabel.CENTER);

        this.resetButton = new JButton(App.LANG_HANDLER.getString("main_resetRouteButton"));
        this.resetButton.setBounds(10, 110, 90, 30);
        this.searchButton = new JButton(App.LANG_HANDLER.getString("main_searchRouteButton"));
        this.searchButton.setBounds(100, 110, 100, 30);
    }

    public void addAll() {
        this.add(this.label1);
        this.add(this.label2);
        this.add(this.field1);
        this.add(this.field2);
        this.add(this.directionLabel);
        this.add(this.resetButton);
        this.add(this.searchButton);
    }

    public void addListeners() {
        this.field1.addActionListener(this);
        this.field1.setActionCommand("search");
        this.field2.addActionListener(this);
        this.field2.setActionCommand("search");
        this.resetButton.addActionListener(this);
        this.resetButton.setActionCommand("reset");
        this.searchButton.addActionListener(this);
        this.searchButton.setActionCommand("search");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
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
}
