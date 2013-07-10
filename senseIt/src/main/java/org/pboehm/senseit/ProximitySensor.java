package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class ProximitySensor extends BaseSensorHandler {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("" + sensorEvent.values[0]);
    }

    @Override
    protected String getSensorTitle() {
        return "Proximity Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_PROXIMITY;
    }

    @Override
    protected String getUnit() {
        return "(cm)";
    }
}