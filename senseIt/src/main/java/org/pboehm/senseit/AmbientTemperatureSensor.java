package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class AmbientTemperatureSensor extends BaseSensorHandler {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("" + sensorEvent.values[0]);
    }

    @Override
    protected String getSensorTitle() {
        return "Ambient Temperature Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_AMBIENT_TEMPERATURE;
    }

    @Override
    protected String getUnit() {
        return "\u00B0C";
    }
}