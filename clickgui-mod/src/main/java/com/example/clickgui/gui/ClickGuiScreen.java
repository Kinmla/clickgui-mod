package com.example.clickgui.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Purely visual "ClickGUI" style settings screen. Categories are drawn as
 * dark rounded panels in a row, each with a header and a list of module
 * rows. Rows have a toggle switch on the right and, if expanded, a slider
 * underneath. None of the modules do anything - wire Module.enabled /
 * Module.actualSliderValue() up to real logic if you want that.
 */
public class ClickGuiScreen extends Screen {

    // ---- palette, tweak these to restyle everything at once ----
    private static final int COLOR_BG          = 0xCC1a1b26; // panel background
    private static final int COLOR_HEADER      = 0xFF2a2b3d; // panel header strip
    private static final int COLOR_ROW_HOVER    = 0x332f6fff;
    private static final int COLOR_ROW_SELECTED = 0x552f6fff;
    private static final int COLOR_ACCENT       = 0xFF2f6fff; // toggle-on / slider fill
    private static final int COLOR_TRACK_OFF    = 0xFF44465a;
    private static final int COLOR_TEXT         = 0xFFE6E6F0;
    private static final int COLOR_TEXT_DIM     = 0xFFA0A0B5;

    private static final int PANEL_WIDTH   = 156;
    private static final int HEADER_HEIGHT = 22;
    private static final int ROW_HEIGHT    = 20;
    private static final int SLIDER_HEIGHT = 24;
    private static final int PANEL_GAP     = 8;

    private final List<Category> categories = new ArrayList<>();

    // drag state for moving panels around
    private Category dragging;
    private int dragOffX, dragOffY;

    public ClickGuiScreen() {
        super(Text.literal("ClickGUI"));
        buildLayout();
    }

    /** Placeholder categories/modules - rename or replace freely. */
    private void buildLayout() {
        int startX = 12;
        int y = 30;

        Category hud = new Category("HUD", startX, y);
        hud.add(new Module("Coordinates"));
        hud.add(new Module("FPS Display"));
        hud.add(new Module("Clock"));
        hud.add(new Module("Armor Status"));
        hud.add(new Module("Potion Timers"));
        categories.add(hud);

        Category render = new Category("Render", startX + (PANEL_WIDTH + PANEL_GAP), y);
        render.add(new Module("Custom Crosshair"));
        render.add(new Module("Toast Notifications"));
        render.add(new Module("Scoreboard"));
        render.add(new Module("Chat Background"));
        categories.add(render);

        Category player = new Category("Player", startX + 2 * (PANEL_WIDTH + PANEL_GAP), y);
        player.add(new Module("Cape"));
        player.add(new Module("Elytra Trail Color"));
        player.add(new Module("Hotbar Animation"));
        categories.add(player);

        Category misc = new Category("Misc", startX + 3 * (PANEL_WIDTH + PANEL_GAP), y);
        misc.add(new Module("Keystrokes Display"));
        misc.add(new Module("Session Timer"));
        misc.add(new Module("Auto Message"));
        categories.add(misc);

        Category settings = new Category("Settings", startX + 4 * (PANEL_WIDTH + PANEL_GAP), y);
        settings.add(new Module("GUI Scale").withSlider(0.5f, 2.0f, 1.0f));
        settings.add(new Module("Panel Opacity").withSlider(0.0f, 1.0f, 0.8f));
        settings.add(new Module("Reset Colors"));
        categories.add(settings);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);

        for (Category cat : categories) {
            renderCategory(ctx, cat, mouseX, mouseY);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderCategory(DrawContext ctx, Category cat, int mouseX, int mouseY) {
        int x = cat.x;
        int y = cat.y;
        int visibleRows = cat.collapsed ? 0 : cat.modules.size();
        int extraHeight = 0;
        if (!cat.collapsed) {
            for (Module m : cat.modules) {
                if (m.hasSlider && m.expanded) extraHeight += SLIDER_HEIGHT;
            }
        }
        int bodyHeight = visibleRows * ROW_HEIGHT + extraHeight;
        int totalHeight = HEADER_HEIGHT + bodyHeight;

        // panel background
        ctx.fill(x, y, x + PANEL_WIDTH, y + totalHeight, COLOR_BG);
        // header
        ctx.fill(x, y, x + PANEL_WIDTH, y + HEADER_HEIGHT, COLOR_HEADER);
        ctx.drawText(this.textRenderer, cat.title, x + 8, y + 7, COLOR_TEXT, false);
        // collapse indicator
        ctx.drawText(this.textRenderer, cat.collapsed ? "+" : "-",
                x + PANEL_WIDTH - 14, y + 7, COLOR_TEXT_DIM, false);

        if (cat.collapsed) return;

        int rowY = y + HEADER_HEIGHT;
        for (Module m : cat.modules) {
            boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH
                    && mouseY >= rowY && mouseY <= rowY + ROW_HEIGHT;

            if (m.enabled) {
                ctx.fill(x, rowY, x + PANEL_WIDTH, rowY + ROW_HEIGHT, COLOR_ROW_SELECTED);
            } else if (hovered) {
                ctx.fill(x, rowY, x + PANEL_WIDTH, rowY + ROW_HEIGHT, COLOR_ROW_HOVER);
            }

            ctx.drawText(this.textRenderer, m.name, x + 8, rowY + 6,
                    m.enabled ? COLOR_TEXT : COLOR_TEXT_DIM, false);

            // toggle switch, top-right of the row
            drawToggle(ctx, x + PANEL_WIDTH - 28, rowY + 5, m.enabled);

            rowY += ROW_HEIGHT;

            if (m.hasSlider && m.expanded) {
                drawSlider(ctx, x + 8, rowY + 4, PANEL_WIDTH - 16, m);
                rowY += SLIDER_HEIGHT;
            }
        }
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean on) {
        int w = 20, h = 10;
        ctx.fill(x, y, x + w, y + h, on ? COLOR_ACCENT : COLOR_TRACK_OFF);
        int knobX = on ? x + w - h : x;
        ctx.fill(knobX, y, knobX + h, y + h, 0xFFFFFFFF);
    }

    private void drawSlider(DrawContext ctx, int x, int y, int width, Module m) {
        int trackH = 4;
        int trackY = y + 6;
        ctx.fill(x, trackY, x + width, trackY + trackH, COLOR_TRACK_OFF);
        int fillW = (int) (width * m.sliderValue);
        ctx.fill(x, trackY, x + fillW, trackY + trackH, COLOR_ACCENT);

        int knobX = x + fillW - 3;
        ctx.fill(knobX, trackY - 3, knobX + 6, trackY + trackH + 3, 0xFFFFFFFF);

        String label = String.format("%.2f", m.actualSliderValue());
        ctx.drawText(this.textRenderer, label, x + width - this.textRenderer.getWidth(label),
                y - 9, COLOR_TEXT_DIM, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Category cat : categories) {
            int x = cat.x, y = cat.y;

            // header click: drag start, or collapse toggle if clicking the +/- glyph
            if (mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + HEADER_HEIGHT) {
                if (mouseX >= x + PANEL_WIDTH - 18) {
                    cat.collapsed = !cat.collapsed;
                } else {
                    dragging = cat;
                    dragOffX = (int) (mouseX - x);
                    dragOffY = (int) (mouseY - y);
                }
                return true;
            }

            if (cat.collapsed) continue;

            int rowY = y + HEADER_HEIGHT;
            for (Module m : cat.modules) {
                if (mouseY >= rowY && mouseY <= rowY + ROW_HEIGHT
                        && mouseX >= x && mouseX <= x + PANEL_WIDTH) {
                    if (m.hasSlider && mouseX >= x + PANEL_WIDTH - 34) {
                        // small area near the toggle still toggles; clicking name area
                        // with a slider module expands/collapses it instead
                        m.enabled = !m.enabled;
                    } else if (m.hasSlider) {
                        m.expanded = !m.expanded;
                    } else {
                        m.enabled = !m.enabled;
                    }
                    return true;
                }
                rowY += ROW_HEIGHT;
                if (m.hasSlider && m.expanded) {
                    if (mouseY >= rowY && mouseY <= rowY + SLIDER_HEIGHT
                            && mouseX >= x + 8 && mouseX <= x + PANEL_WIDTH - 8) {
                        updateSlider(m, mouseX, x + 8, PANEL_WIDTH - 16);
                        return true;
                    }
                    rowY += SLIDER_HEIGHT;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging != null) {
            dragging.x = (int) (mouseX - dragOffX);
            dragging.y = (int) (mouseY - dragOffY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateSlider(Module m, double mouseX, int trackX, int trackWidth) {
        float value = (float) ((mouseX - trackX) / trackWidth);
        m.sliderValue = Math.max(0f, Math.min(1f, value));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
