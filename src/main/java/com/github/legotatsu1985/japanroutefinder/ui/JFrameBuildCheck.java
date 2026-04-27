package com.github.legotatsu1985.japanroutefinder.ui;

import java.awt.event.ActionListener;

public interface JFrameBuildCheck extends ActionListener {
    void initComponents();
    void addAll();
    void addListeners();
    void setDevelopmentMode(boolean b);
}
