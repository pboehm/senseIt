package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class GyroscopeSensor extends BaseSensorHandler {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("x = " + sensorEvent.values[0]);
        value2.setText("y = " + sensorEvent.values[1]);
        value3.setText("z = " + sensorEvent.values[2]);
    }

    @Override
    protected String getSensorTitle() {
        return "Gyroscope Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_GYROSCOPE;
    }

    @Override
    protected String getUnit() {
        return "rad/s";
    }
}