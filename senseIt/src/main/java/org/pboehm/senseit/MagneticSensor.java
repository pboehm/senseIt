package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class MagneticSensor extends BaseSensorHandler {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("x = " + sensorEvent.values[0]);
        value2.setText("y = " + sensorEvent.values[1]);
        value3.setText("z = " + sensorEvent.values[2]);
    }

    @Override
    protected String getSensorTitle() {
        return "Magnetic Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_MAGNETIC_FIELD;
    }

    @Override
    protected String getUnit() {
        return "\u03BCT";
    }
}