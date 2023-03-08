package com.vulpuslabs.vulpes.modules.chebz;

import java.awt.*;
import java.awt.geom.Path2D;

public class ChebzView {

    private static final Stroke WAVEFORM_STROKE = new BasicStroke(
        2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND
    );

    private final ChebzModel model;
    private final int width;
    private final int height;
    private final double midHeight;
    private final double delta;

    public ChebzView(ChebzModel model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
        this.delta = 2.0 / width;
        this.midHeight = height * 0.5;
    }

    public void redraw(Graphics2D g) {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, width, height);

        g.setColor(Color.GREEN);

        g.setStroke(WAVEFORM_STROKE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        var path = new Path2D.Double();
        var pos = 0.0;

        path.moveTo(0.0, midHeight * (1.0 + (model.getSampleAt(0.0) * 0.5)));
        pos += delta;
        
        for (int i=1; i<width; i++) {
            var waveFormPoint = model.getSampleAt(pos) * 0.5;
            pos += delta;
            path.lineTo(i, midHeight * (1.0 + waveFormPoint));
        }

        g.draw(path);
    }
}
