package com.github.legotatsu1985.japanroutefinder.ui.panels;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.ui.JFrameBuildCheck;
import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

public class RoutePanel extends JPanel implements JFrameBuildCheck {
    private LineBorder panelBorder;
    private JTextField routeField;
    private JLabel remarkLabel, timeRestrictionLabel;
    private JButton copyRouteButton;

    private Route route;

    public RoutePanel(@NotNull Route route) {
        super();
        this.route = route;
        initComponents();
        addAll();
        addListeners();
        setDevelopmentMode(App.CFG.isDevelopmentMode());
    }

    public void initComponents() {
        this.setLayout(null);
        this.setMaximumSize(new Dimension(2000, 100));
        // this.setBackground(Color.WHITE);
        this.routeField = new JTextField(this.route.getRoute());
        this.routeField.setBounds(5, 5, 850, 30);
        this.routeField.setEditable(false);
        this.routeField.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.remarkLabel = new JLabel(this.route.getRemark());
        this.remarkLabel.setBounds(5, 40, 850, 20);
        this.timeRestrictionLabel = new JLabel(this.route.getTimeRestriction());
        this.timeRestrictionLabel.setBounds(5, 65, 850, 20);
        this.copyRouteButton = new JButton("Copy");
        this.copyRouteButton.setBounds(860, 5, 100, 30);
    }

    public void addAll() {
        this.add(routeField);
        this.add(remarkLabel);
        this.add(timeRestrictionLabel);
        this.add(copyRouteButton);
    }

    public void addListeners() {
        this.copyRouteButton.addActionListener(this);
        this.copyRouteButton.setActionCommand("copy");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "copy":
                StringSelection stringSelection = new StringSelection(this.route.getRoute());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                System.out.printf("Route copied to clipboard: %s\n", this.route.getRoute());
                break;
            default: break;
        }
    }

    public void setDevelopmentMode(boolean b) {
        if (b) {
            this.setBorder(new LineBorder(Color.CYAN));
        } else {
            this.setBorder(new LineBorder(Color.BLACK));
        }
        this.revalidate();
        this.repaint();
    }
}
