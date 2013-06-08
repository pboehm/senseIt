package org.pboehm.senseit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class BaseSensorHandler extends Activity implements SensorEventListener {

    protected SensorManager manager;
    protected Sensor sensor;
    protected TextView sensor_title, value1, value2, value3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_values);

        sensor_title = (TextView) findViewById(R.id.text_sensor_type);
        sensor_title.setText(getSensorTitle());

        value1 = (TextView) findViewById(R.id.text_for_value1);
        value2 = (TextView) findViewById(R.id.text_for_value2);
        value3 = (TextView) findViewById(R.id.text_for_value3);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(getSensorType());
        if (sensor == null) {
            Toast.makeText(getApplicationContext(),
                    "Sensor is not available", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    protected String getSensorTitle() {
        return "Title not set";
    }

    protected int getSensorType() {
        return -1;
    }
}