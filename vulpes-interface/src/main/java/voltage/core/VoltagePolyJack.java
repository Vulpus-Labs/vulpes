package voltage.core;

public abstract class VoltagePolyJack extends VoltageComponent {

    public abstract double GetPolyValue(int polyChannel);
    public abstract void SetPolyValue(int polyChannel, double value);

}
