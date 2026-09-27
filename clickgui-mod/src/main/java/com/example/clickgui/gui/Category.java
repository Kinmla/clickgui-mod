package com.example.clickgui.gui;

import java.util.ArrayList;
import java.util.List;

/** One column/panel in the ClickGUI, e.g. "HUD" or "Render". */
public class Category {
    public final String title;
    public final List<Module> modules = new ArrayList<>();

    // layout state
    public int x;
    public int y;
    public boolean collapsed = false;

    public Category(String title, int x, int y) {
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public Category add(Module m) {
        modules.add(m);
        return this;
    }
}
