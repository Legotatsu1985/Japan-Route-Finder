package com.github.legotatsu1985.japanroutefinder;

import com.github.legotatsu1985.japanroutefinder.handlers.*;
import com.github.legotatsu1985.japanroutefinder.ui.*;
import com.github.legotatsu1985.japanroutefinder.util.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class App {
    public static final String APP_CONFIG_PROPERTIES_PATH = "config.properties";
    public static LangHandler LANG_HANDLER;
    public static final Config CFG = new Config(APP_CONFIG_PROPERTIES_PATH);
    public static UIHandler UI;
    public static void main(String[] args) {
        String langCode = CFG.getLangCode();
        LANG_HANDLER = new LangHandler(langCode);
        UI = new UIHandler();
        UI.openMain();
    }
    public static void popUpBackgroundError(@NotNull Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
    }
}
