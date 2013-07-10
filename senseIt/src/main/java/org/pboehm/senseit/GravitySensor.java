package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class GravitySensor extends BaseSensorHandler {

    protected int sensor_type = Sensor.TYPE_GRAVITY;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("x = " + sensorEvent.values[0]);
        value2.setText("y = " + sensorEvent.values[1]);
        value3.setText("z = " + sensorEvent.values[2]);
    }


    @Override
    protected String getSensorTitle() {
        return "Gravity Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_GRAVITY;
    }

    @Override
    protected String getUnit() {
        return "m/s\u00B2";
    }
}