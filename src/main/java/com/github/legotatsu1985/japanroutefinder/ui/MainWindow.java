package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.handlers.RouteHandler;
import com.github.legotatsu1985.japanroutefinder.ui.menubars.MainMenuBar;
import com.github.legotatsu1985.japanroutefinder.ui.panels.*;
import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {
    private JPanel basePanel;
    private SearchPanel searchPanel;
    private RouteResultPanel routeResultPanel;
    private JScrollPane scrollPane;
    private MainMenuBar mainMenuBar;

    public MainWindow() {
        super("Japan Route Finder");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initComponents();
    }
    private void initComponents() {
        // Panels
        this.basePanel = new JPanel();
        // this.basePanel.setLayout(new BoxLayout(this.basePanel, BoxLayout.Y_AXIS));
        this.basePanel.setLayout(null);
        this.searchPanel = new SearchPanel(this);
        this.searchPanel.setBounds(0, 0, 985, 150);

        // Menu Bar
        this.mainMenuBar = new MainMenuBar();
        this.setJMenuBar(this.mainMenuBar);

        // Scroll Pane
        this.scrollPane = new JScrollPane();
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add Panels to Frame
        this.basePanel.add(this.searchPanel);
        this.basePanel.add(this.scrollPane);
        this.add(this.basePanel);
    }
    public void searchRoute() {
        if (this.routeResultPanel != null) {
            this.basePanel.remove(this.routeResultPanel);
            this.revalidate();
            this.repaint();
        }

        String origin = this.searchPanel.getOrigin();
        String destination = this.searchPanel.getDestination();
        if (origin == null || destination == null || origin.isEmpty() || destination.isEmpty()) {
            App.UI.popupError(this, new IllegalStateException("Origin or destination cannot be null"));
            return;
        }
        System.out.println("Start searching for: " + origin + " -> " + destination + "...");
        RouteHandler rh = new RouteHandler(this, App.CFG.getXlsxFilePath());
        rh.find(origin, destination);
        List<Route> routes = rh.getAllRoutes();
        if (rh.getRouteCount() == 0 || routes.isEmpty()) {
            App.UI.popupError(this, new IllegalStateException("No routes found"));
            return;
        }
        this.routeResultPanel = new RouteResultPanel(routes);
        this.scrollPane.setViewportView(this.routeResultPanel);
        this.scrollPane.setBounds(0, 150, 985, 410);
        this.routeResultPanel.revalidate();
        this.routeResultPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    @Nullable
    private String chooseXlsxFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Excel File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel File", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null; // User canceled the operation
        }
    }
}
