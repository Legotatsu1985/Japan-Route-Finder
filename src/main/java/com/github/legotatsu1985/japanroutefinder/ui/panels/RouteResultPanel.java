package com.github.legotatsu1985.japanroutefinder.ui.panels;

import com.github.legotatsu1985.japanroutefinder.util.Route;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class RouteResultPanel extends JPanel {
    private List<RoutePanel> routePanels;

    public RouteResultPanel(@NotNull List<Route> routes) {
        super();
        this.routePanels = new LinkedList<>();
        for(Route route : routes) {
            this.routePanels.add(new RoutePanel(route));
        }
        this.setLayout(new GridLayout(0, 1, 10, 10));
        this.setPreferredSize(new Dimension(970, routePanels.size() * 110));
        this.setBorder(new LineBorder(Color.GREEN));
        initComponents();
        addAll();
    }
    private void initComponents() {this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));}

    private void addAll() {
        for (RoutePanel routePanel : routePanels) {
            this.add(routePanel);
        }
    }

    public int getRouteCount() {return this.routePanels.size();}
}
