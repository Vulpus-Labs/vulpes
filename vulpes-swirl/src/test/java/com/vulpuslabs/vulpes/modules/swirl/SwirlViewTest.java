package com.vulpuslabs.vulpes.modules.swirl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SwirlViewTest {

    @Test
    public void initialises() throws IOException {
        var model = new SwirlModel(16);
        var logo = ByteBuffer.wrap(getClass()
                .getResourceAsStream("logo_green.png")
                .readAllBytes());
        var view = new Graphics2dSwirlView(logo, model, 8, 200, 200);
    }
}
