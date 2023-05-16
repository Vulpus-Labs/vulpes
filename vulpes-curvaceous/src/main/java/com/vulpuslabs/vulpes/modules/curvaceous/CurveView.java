package com.vulpuslabs.vulpes.modules.curvaceous;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class CurveView {

    private static final RescaleOp op = new RescaleOp(
            new float[] { 1.0f, 1.0f, 1.0f, 1.0f },
            new float[] { 0.0f, 0.0f, 0.0f, -25f },
            null);

    private static final Color SONAR_GREEN = new Color(64, 255, 64, 128);
    private final BufferedImage plot;
    private final Graphics2D plotGraphics;
    private final int width;
    private final int height;
    private final double widthScale;
    private final double heightScale;

    public CurveView(int width, int height) {
        this.width = width;
        this.height = height;
        widthScale = 0.1 * (width - 1);
        heightScale = 0.1 *  (height - 1);

        plot = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        plotGraphics = plot.createGraphics();
        plotGraphics.setBackground(new Color(0, 0, 0, 0));
        plotGraphics.clearRect(0, 0, width, height);
        plotGraphics.setColor(SONAR_GREEN);
        plotGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void plotPoint(double x, double y) {
        plot.setRGB((int) ((x + 5.0) * widthScale),
                height - 1 - (int) ((y + 5.0) * heightScale),
                SONAR_GREEN.getRGB());
    }

    public void drawCurve(Graphics2D g) {
        g.clearRect(0, 0, width, height);
        op.filter(plot, plot);
        g.drawImage(plot, 0, 0, null);
    }
}
