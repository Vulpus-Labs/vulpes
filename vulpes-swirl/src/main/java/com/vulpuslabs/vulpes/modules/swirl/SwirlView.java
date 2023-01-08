package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.FakeTrig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SwirlView {

    private static final RescaleOp op = new RescaleOp(
            new float[] { 1.0f, 1.0f, 1.0f, 1.0f },
            new float[] { 0.0f, 0.0f, 0.0f, -25f },
            null);

    private static final Ellipse2D.Float circle = new Ellipse2D.Float(0, 0, 0, 0);
    private static final Color SONAR_GREEN = new Color(64, 255, 64, 128);
    private static final Line2D.Double line = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
    private static final Color[] colors = new Color[] {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.YELLOW,
            Color.PINK,
            Color.RED,
            Color.BLUE.brighter(),
            Color.CYAN.brighter(),
            Color.GREEN.brighter(),
            Color.MAGENTA.brighter(),
            Color.ORANGE.brighter(),
            Color.YELLOW.brighter(),
            Color.PINK.brighter(),
            Color.RED.brighter(),
    };
    private final SwirlModel model;
    private final int width;
    private final int height;
    private final int centreX;
    private final int centreY;
    private final double radiusScaling;
    private BufferedImage background;
    private BufferedImage plot;
    private Graphics2D plotGraphics;
    private BufferedImage logo;

    private int activeSize;
    private boolean drawHeads;

    public SwirlView(ByteBuffer logoBytes, SwirlModel model, int activeSize, int width, int height) {
        this.model = model;
        this.activeSize = activeSize;
        this.width = width;
        this.height = height;
        this.centreX = width / 2;
        this.centreY = height / 2;
        this.radiusScaling = Math.min(centreX - 1, centreY - 1) / 5.0;
        this.drawHeads = true;

        try {
            logoBytes.rewind();
            byte[] arr = new byte[logoBytes.remaining()];
            logoBytes.get(arr, 0, logoBytes.remaining());
            logo = ImageIO.read(new ByteArrayInputStream(arr));
        } catch (IllegalArgumentException | IOException ignored) {
        }

        initializePlot();
    }

    public void setDrawHeads(boolean drawHeads) {
        this.drawHeads = drawHeads;
    }

    public void setActiveSize(int activeSize) {
        this.activeSize = activeSize;
    }

    private void initializePlot() {
        plot = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        plotGraphics = plot.createGraphics();
        plotGraphics.setBackground(new Color(0, 0, 0, 0));
        plotGraphics.clearRect(0, 0, width, height);
        plotGraphics.setColor(SONAR_GREEN);
        plotGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public void plotIntermediate() {
        model.getCartesian(activeSize, (i, x, y) -> {
            var newX = (int) (centreX + (radiusScaling * x));
            var newY = (int) (centreY + (radiusScaling * y));

            plot.setRGB(newX, newY, colors[i].getRGB());
        });
    }

    public void redraw(Graphics2D g) {
        if (background == null) {
            drawBackground();
        }

        g.drawImage(background, 0, 0, null);
        op.filter(plot, plot);

        if (drawHeads) {
            model.getCartesian(activeSize, (i, x, y) -> {
                var newX = centreX + (radiusScaling * x);
                var newY = centreY + (radiusScaling * y);
                plotGraphics.setColor(colors[i]);
                circle.setFrame(newX - 2, newY - 2, 4, 4);
                plotGraphics.fill(circle);
            });
        }

        g.drawImage(plot, 0, 0, null);
    }

    private void drawBackground() {
        background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = background.createGraphics();
        g.setColor(SONAR_GREEN);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        if (logo != null) {
            var alphaLogo = new BufferedImage(logo.getWidth(), logo.getHeight(), BufferedImage.TYPE_INT_ARGB);
            RescaleOp op = new RescaleOp(
                    new float[] { 1.0f, 1.0f, 1.0f, 1.0f },
                    new float[] { 0.0f, 0.0f, 0.0f, -160f },
                    null);
            op.filter(logo, alphaLogo);
            g.drawImage(alphaLogo,
                    centreX - (logo.getWidth() / 2),
                    centreY - (logo.getHeight() / 2),
                    null);
        }
        double maxRadius = Math.min(centreX, centreY) - 1;

        double radius = maxRadius / 8;
        double radiusDelta = radius;
        for (int i = 1; i <= 8; i++) {
            var shape = new Ellipse2D.Double(
                    centreX - radius,
                    centreY - radius,
                    radius * 2,
                    radius * 2);
            g.draw(shape);
            radius += radiusDelta;
        }

        double angle = 0.0;
        double angleDelta = 1.0 / 12.0;

        for (int i=0; i<12; i++) {
            g.drawLine(
                    centreX,
                    centreY,
                    (int) (centreX + (maxRadius * FakeTrig.fakeCosUnit(angle))),
                    (int) (centreY + (maxRadius * FakeTrig.fakeSinUnit(angle))));
            angle += angleDelta;
        }

        angle = 0.0;
        angleDelta = Math.PI * 2.0 / 96.0;
        double innerRadius = maxRadius * 0.98;

        for (int i=0; i<96; i++) {
            line.setLine(centreX + (innerRadius * Math.cos(angle)),
                    centreY + (innerRadius * Math.sin(angle)),
                    centreX + (maxRadius * Math.cos(angle)),
                    centreY + (maxRadius * Math.sin(angle)));
            g.draw(line);
            angle += angleDelta;
        }
    }
}
