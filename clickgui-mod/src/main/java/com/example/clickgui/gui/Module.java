package com.example.clickgui.gui;

/**
 * A single placeholder entry in a category panel: a name, an on/off state,
 * and an optional slider value (0.0 - 1.0). Nothing here does anything by
 * itself - hook these up to whatever real feature you want, or leave them
 * as-is purely for the look.
 */
public class Module {
    public final String name;
    public boolean enabled;
    public boolean hasSlider;
    public float sliderValue;      // 0.0 - 1.0
    public float sliderMin;
    public float sliderMax;
    public boolean expanded;       // whether the slider row is shown

    public Module(String name) {
        this.name = name;
    }

    public Module withSlider(float min, float max, float defaultValue) {
        this.hasSlider = true;
        this.sliderMin = min;
        this.sliderMax = max;
        this.sliderValue = (defaultValue - min) / (max - min);
        return this;
    }

    public float actualSliderValue() {
        return sliderMin + sliderValue * (sliderMax - sliderMin);
    }
}
