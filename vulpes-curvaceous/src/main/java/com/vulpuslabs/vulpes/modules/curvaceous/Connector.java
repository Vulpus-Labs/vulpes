package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.inputs.InputOrKnob;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

public class Connector {

    private final UIEventConnector connector;

    public Connector(UIEventConnector connector) {
        this.connector = connector;
    }

    public CurveControl.Segment startSegment(VoltageAudioJack yIn,
                                             VoltageComponent yKnob,
                                             VoltageComponent curveKnob) {
        InputOrKnob y = InputOrKnob.connect(connector, yIn, yKnob);
        CurveControl.Segment segment = new CurveControl.Segment(() -> -5.0, y);
        connector.connectUnsmoothedKnob(curveKnob, segment::setCurveType);

        return segment;
    }

    public CurveControl.Segment endSegment(VoltageAudioJack yIn,
                                             VoltageComponent yKnob) {
        InputOrKnob y = InputOrKnob.connect(connector, yIn, yKnob);
        CurveControl.Segment segment = new CurveControl.Segment(() -> 5.0, y);
        segment.setCurveType(CurveType.STEP.getSwitchValue());

        return segment;
    }

    public CurveControl.Segment segment(VoltageAudioJack xIn,
                                        VoltageComponent xKnob,
                                        VoltageAudioJack yIn,
                                        VoltageComponent yKnob,
                                        VoltageComponent curveKnob) {
        InputOrKnob x = InputOrKnob.connect(connector, xIn, xKnob);
        InputOrKnob y = InputOrKnob.connect(connector, yIn, yKnob);
        CurveControl.Segment segment = new CurveControl.Segment(x, y);
        connector.connectUnsmoothedKnob(curveKnob, segment::setCurveType);

        return segment;
    }

    public FMControl fmControl(VoltageAudioJack fmInputJack,
                                    VoltageComponent attenuvertorKnob,
                                    Object linExpSwitch) {
        FMControl fmControl = new FMControl(
                connector.connectMonoInput(fmInputJack),
                connector.connectSmoothedKnob(attenuvertorKnob));

        connector.getEventBus().registerBooleanObserver(fmInputJack, fmControl::setIsConnected);
        connector.connectTwoStateSwitch(linExpSwitch, fmControl::setIsExponential);

        return fmControl;
    }

    public PolyFMControl polyFmControl(Object fmInputJack,
                                        VoltageComponent attenuvertorKnob,
                                        Object linExpSwitch) {
        PolyFMControl fmControl = new PolyFMControl(
                connector.connectPolyInput(fmInputJack),
                connector.connectSmoothedKnob(attenuvertorKnob));

        connector.getEventBus().registerBooleanObserver(fmInputJack, fmControl::setIsConnected);
        connector.connectTwoStateSwitch(linExpSwitch, fmControl::setIsExponential);

        return fmControl;
    }

    public SyncControl syncControl(Object syncJack) {
        SyncControl syncControl = new SyncControl(connector.connectPolyInput(syncJack));
        connector.getEventBus().registerBooleanObserver(syncJack, syncControl::setIsConnected);
        return syncControl;
    }
}
