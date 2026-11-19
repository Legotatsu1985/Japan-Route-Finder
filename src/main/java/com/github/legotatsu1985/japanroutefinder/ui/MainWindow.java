package com.github.legotatsu1985.japanroutefinder.ui;

import com.github.legotatsu1985.japanroutefinder.App;
import com.github.legotatsu1985.japanroutefinder.handlers.RouteHandler;
import com.github.legotatsu1985.japanroutefinder.ui.menubars.MainMenuBar;
import com.github.legotatsu1985.japanroutefinder.ui.panels.*;
import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainWindow extends JFrame implements IJFrame {
    private JPanel domesticBasePanel;
    private JPanel internationalOutboundPanel;
    private JPanel internationalInboundPanel;
    private SearchPanel searchPanel;
    private InternationalSearchPanel internationalOutboundSearchPanel;
    private InternationalSearchPanel internationalInboundSearchPanel;
    private RouteResultPanel routeResultPanel;
    private JScrollPane domesticScrollPane;
    private JScrollPane internationalOutboundScrollPane;
    private JScrollPane internationalInboundScrollPane;
    private MainMenuBar mainMenuBar;
    private JTabbedPane mainTabbedPane;

    public MainWindow() {
        super("Japan Route Finder");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initComponents();
        addAll();
        addListeners();
        setDevelopmentMode(App.CFG.isDevelopmentMode());
    }
    public void initComponents() {
        // Panels
        this.domesticBasePanel = new JPanel();
        // this.basePanel.setLayout(new BoxLayout(this.basePanel, BoxLayout.Y_AXIS));
        this.domesticBasePanel.setLayout(null);
        this.internationalOutboundPanel = new JPanel();
        this.internationalOutboundPanel.setLayout(null);
        this.internationalInboundPanel = new JPanel();
        this.internationalInboundPanel.setLayout(null);
        this.searchPanel = new SearchPanel(this);
        this.searchPanel.setBounds(0, 0, 985, 150);
        this.internationalOutboundSearchPanel = new InternationalSearchPanel(this, InternationalSearchPanel.OUTBOUND);
        this.internationalOutboundSearchPanel.setBounds(0, 0, 985, 150);
        this.internationalInboundSearchPanel = new InternationalSearchPanel(this, InternationalSearchPanel.INBOUND);
        this.internationalInboundSearchPanel.setBounds(0, 0, 985, 150);

        // Menu Bar
        this.mainMenuBar = new MainMenuBar();
        this.setJMenuBar(this.mainMenuBar);

        // Tabbed Pane
        this.mainTabbedPane = new JTabbedPane();

        // Scroll Pane
        this.domesticScrollPane = new JScrollPane();
        this.domesticScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.internationalOutboundScrollPane = new JScrollPane();
        this.internationalOutboundScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.internationalInboundScrollPane = new JScrollPane();
        this.internationalInboundScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }
    public void addAll() {
        // Add Panels to Frame
        this.domesticBasePanel.add(this.searchPanel);
        this.domesticBasePanel.add(this.domesticScrollPane);
        this.internationalOutboundPanel.add(this.internationalOutboundSearchPanel);
        this.internationalInboundPanel.add(this.internationalInboundSearchPanel);
        this.internationalOutboundPanel.add(this.internationalOutboundScrollPane);
        this.internationalInboundPanel.add(this.internationalInboundScrollPane);
        this.mainTabbedPane.addTab(App.LANG_HANDLER.getString("main_tabDomestic"), this.domesticBasePanel);
        this.mainTabbedPane.addTab(App.LANG_HANDLER.getString("main_tabInternationalOutbound"), this.internationalOutboundPanel);
        this.mainTabbedPane.addTab(App.LANG_HANDLER.getString("main_tabInternationalInbound"), this.internationalInboundPanel);
        this.add(this.mainTabbedPane);
    }
    public void searchRoute() {
        if (this.routeResultPanel != null) {
            this.domesticBasePanel.remove(this.routeResultPanel);
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
        this.domesticScrollPane.setViewportView(this.routeResultPanel);
        this.domesticScrollPane.setBounds(0, 150, 985, 380);
        this.routeResultPanel.revalidate();
        this.routeResultPanel.repaint();
        this.mainTabbedPane.revalidate();
        this.mainTabbedPane.repaint();
        this.revalidate();
        this.repaint();
    }
    public void addListeners() {}
    public void actionPerformed(ActionEvent e) {}
    public void setDevelopmentMode(boolean b) {
        this.mainMenuBar.setDevelopmentMode(b);
        this.searchPanel.setDevelopmentMode(b);
        if (this.routeResultPanel != null && this.routeResultPanel.isVisible()) {
            this.routeResultPanel.setDevelopmentMode(b);
        }
        this.revalidate();
        this.repaint();
    }

    @Deprecated
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
