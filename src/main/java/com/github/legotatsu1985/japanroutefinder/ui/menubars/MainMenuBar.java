package com.github.legotatsu1985.japanroutefinder.ui.menubars;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.handlers.UIHandler;
import com.github.legotatsu1985.japanroutefinder.ui.JFrameBuildCheck;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainMenuBar extends JMenuBar implements JFrameBuildCheck {
    // Menus
    private JMenu file;

    // Menu Items
    private JMenuItem settingsItem;
    private JMenuItem exitItem;

    public MainMenuBar() {
        super();
        initComponents();
        addAll();
        addListeners();
    }

    public void initComponents() {
        this.file = new JMenu("File");
        this.settingsItem = new JMenuItem("Settings");
        this.exitItem = new JMenuItem("Exit");
    }

    public void addAll() {
        this.file.add(this.settingsItem);
        this.file.add(this.exitItem);
        this.add(this.file);
    }

    public void addListeners() {
        this.settingsItem.addActionListener(_ -> App.UI.openSettings());
        this.exitItem.addActionListener(_ -> System.exit(0));
    }

    public void actionPerformed(ActionEvent e) {}
    public void setDevelopmentMode(boolean b) {}
}
