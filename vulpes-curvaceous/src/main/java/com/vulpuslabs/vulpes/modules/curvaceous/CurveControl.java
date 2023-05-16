package com.vulpuslabs.vulpes.modules.curvaceous;

import java.util.function.DoubleSupplier;

public class CurveControl {

    public static final class Segment {

        private final DoubleSupplier xStartSupplier;
        private  double xStart;
        private final DoubleSupplier yStartSupplier;
        private double yStart;
        private CurveType curveType = CurveType.STEP;

        public Segment(DoubleSupplier xStartSupplier, DoubleSupplier yStartSupplier) {
            this.xStartSupplier = xStartSupplier;
            this.yStartSupplier = yStartSupplier;
        }

        public void setCurveType(double switchValue) {
            curveType = CurveType.valueOf(switchValue);
        }

        public void tick() {
            xStart = xStartSupplier.getAsDouble();
            yStart = yStartSupplier.getAsDouble();
        }

        public double getXStart() {
            return xStart;
        }

        public double getYStart() {
            return yStart;
        }

        public CurveType getCurveType() {
            return curveType;
        }
    }

    private final Segment[] segments;

    public CurveControl(Segment...segmentControls) {
        this.segments = segmentControls;
    }

    public void tick() {
        for (Segment segment : segments) {
            segment.tick();
        }
    }

    public double apply(double x) {

        double xStart = -5.0;
        double yStart = segments[0].getYStart();
        CurveType curveType = segments[0].getCurveType();

        for (int i = 1; i < segments.length; i++) {
            double xEnd = segments[i].getXStart();
            CurveType nextCurveType = segments[i].getCurveType();

            // skip illegal or disabled segments
            if (xStart > xEnd || nextCurveType == CurveType.NONE) continue;

            double yEnd = segments[i].getYStart();
            if (x < xEnd) {
                if (curveType == CurveType.STEP) return yStart;

                double width = xEnd - xStart;
                double pos = curveType.apply((x - xStart) / width);
                return yStart * (1.0 - pos) + yEnd * pos;
            }

            xStart = xEnd;
            yStart = yEnd;
            curveType = nextCurveType;
        }

        return yStart;
    }
}
