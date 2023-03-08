package com.vulpuslabs.vulpes.modules.switcheroo;

import java.awt.*;

public class GridView {

    private static final Color UNLIT_GREY = new Color(24, 24, 24, 255);
    private static final Color LIT_GREY = new Color(72, 72, 72, 255);
    private static final Color LIT_GREY_NIMBUS = new Color(72, 72, 72, 128);
    private static final Color UNLIT_GREEN = new Color(72, 72, 24, 255);
    private static final Color LIT_GREEN = new Color(176, 176, 72, 255);
    private static final Color LIT_GREEN_NIMBUS = new Color(176, 176, 72, 128);
    private static final float[] KEYFRAMES = new float[] { 0.1f, 1f };
    private static final Color[] SELECTED_GRAD = new Color[] { LIT_GREEN, UNLIT_GREEN };
    private static final Color[] UNSELECTED_GRAD = new Color[] { LIT_GREY, UNLIT_GREY };

    private final GridModel model;
    private final int width;
    private final int height;
    private final int cellWidth;
    private final int cellHeight;
    private final float lightOffsetX;
    private final float lightOffsetY;
    private final float lightRadius;

    public GridView(GridModel model, int width, int height) {
        this.model = model;

        this.width = width;
        this.height = height;
        this.cellWidth = width / 16;
        this.cellHeight = height / 4;

        this.lightOffsetX = cellWidth * 0.4f;
        this.lightOffsetY = cellHeight * 0.4f;
        this.lightRadius = cellHeight * 0.75f;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, width, height, true);

        int topLeftX = 0;
        for (int x=0; x<16; x++) {
            drawStep(g, topLeftX, x);
            topLeftX += cellWidth;
        }
    }

    private void drawStep(Graphics2D g, int topLeftX, int x) {
        int topLeftY = 0;
        int lit = model.getSelectedChannel(x);
        boolean isCurrentStep = x == model.getCurrentStep();

        for (int y=0; y<4; y++) {
            drawCell(g, topLeftX, isCurrentStep, topLeftY, y == lit, y);
            topLeftY += cellHeight;
        }
    }

    private void drawCell(Graphics2D g, int topLeftX, boolean isCurrentStep, int topLeftY, boolean isLit, int y) {
        if (isCurrentStep) {
            drawCellWithGradient(g, topLeftX, topLeftY, isLit);
        } else {
            drawCellWithoutGradient(g, topLeftX, topLeftY, isLit);
        }

        g.setColor(isLit ? LIT_GREEN_NIMBUS : LIT_GREY_NIMBUS);
        g.drawRect(topLeftX + 2, topLeftY + 2, cellWidth - 4, cellHeight - 4);
    }

    private void drawCellWithoutGradient(Graphics2D g, int topLeftX, int topLeftY, boolean isLit) {
        g.setColor(isLit ? UNLIT_GREEN : UNLIT_GREY);
        g.fillRect(topLeftX + 2, topLeftY + 2, cellWidth - 4, cellHeight - 4);
    }

    private void drawCellWithGradient(Graphics2D g, int topLeftX, int topLeftY, boolean isLit) {
        Paint old = g.getPaint();
        g.setPaint(new RadialGradientPaint(
                topLeftX + lightOffsetX,
                topLeftY + lightOffsetY,
                lightRadius,
                KEYFRAMES,
                isLit ? SELECTED_GRAD : UNSELECTED_GRAD));
        g.fillRect(topLeftX + 2, topLeftY + 2, cellWidth - 4, cellHeight - 4);
        g.setPaint(old);
    }

    public int getStep(int mouseX) {
        return mouseX / cellWidth;
    }

    public int getChannel(int mouseY) {
        return mouseY / cellHeight;
    }
}
