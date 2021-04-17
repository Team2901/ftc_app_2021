package org.firstinspires.ftc.teamcode.Shared.Hardware;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.qualcomm.robotcore.R.string;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@AnalogSensorType
@DeviceProperties(
        name = "@string/configTypeAnalogInput",
        xmlTag = "AnalogInput",
        builtIn = true
)
public class MockAnalogInput extends AnalogInput {
    private AnalogInputController controller = null;
    private int channel = -1;

    public MockAnalogInput() {
        super(null, 0);
    }

    public Manufacturer getManufacturer() {
        return null;
    }

    public double getVoltage() {
        return 2;
    }

    public double getMaxVoltage() {
        return 0;
    }

    public String getDeviceName() {
        return null;
    }

    public String getConnectionInfo() {
        return null;
    }

    public int getVersion() {
        return 0;
    }

    public void resetDeviceConfigurationForOpMode() {
    }

    public void close() {
    }
}
