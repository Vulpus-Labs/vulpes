package com.vulpuslabs.vulpes.modules.swirl;

import java.awt.*;

public interface SwirlView {
    void setDrawHeads(boolean drawHeads);

    void setActiveSize(int activeSize);

    void plotIntermediate();

    void redraw(Graphics2D g);
}
