package voltage.core;

public abstract class VoltageComponent {

    public abstract void SetValue(double newValue);
    public abstract void SetValue(double newValue, boolean bUpdateGUI);

    public abstract void SetValueNoNotification(double newValue, boolean bUpdateGUI);
    public abstract double GetValue();

    public abstract double GetMinValue();
    public abstract double GetMaxValue();

}
