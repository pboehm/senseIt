package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class LightSensor extends BaseSensorHandler {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("" + sensorEvent.values[0]);
    }

    @Override
    protected String getSensorTitle() {
        return "Light Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_LIGHT;
    }
}