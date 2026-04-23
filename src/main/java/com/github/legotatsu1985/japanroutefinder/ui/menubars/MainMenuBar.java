package com.github.legotatsu1985.japanroutefinder.ui.menubars;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.handlers.UIHandler;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {
    // Menus
    private JMenu file;

    // Menu Items
    private JMenuItem settingsItem;
    private JMenuItem exitItem;

    public MainMenuBar() {
        super();
        initComponents();
        build();
        setActionListeners();
    }

    private void initComponents() {
        this.file = new JMenu("File");
        this.settingsItem = new JMenuItem("Settings");
        this.exitItem = new JMenuItem("Exit");
    }

    private void build() {
        this.file.add(this.settingsItem);
        this.file.add(this.exitItem);
        this.add(this.file);
    }

    public void setActionListeners() {
        this.settingsItem.addActionListener(_ -> App.UI.openSettings());
        this.exitItem.addActionListener(_ -> System.exit(0));
    }
}
