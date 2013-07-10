package org.pboehm.senseit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableSensorsActivity extends Activity implements AdapterView.OnItemClickListener {

    SensorManager manager;
    List<Sensor> available_sensors;
    ListView view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sensors);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        view = (ListView) findViewById(R.id.list_available_sensors);
        available_sensors = manager.getSensorList(Sensor.TYPE_ALL);

        SimpleAdapter adapter = new SimpleAdapter(this,
                getFormattedSensorData(),
                android.R.layout.simple_list_item_2,
                new String[]{"name", "description"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        view.setAdapter(adapter);
        view.setOnItemClickListener(this);
    }

    public ArrayList<Map<String, String>> getFormattedSensorData() {
        ArrayList<Map<String, String>> sensors = new ArrayList<Map<String, String>>();

        for (Sensor s : available_sensors) {
            Map<String, String> sensor = new HashMap<String, String>();

            String description = "Vendor: " + s.getVendor();

            description += "\nPower (when in action): " + s.getPower() + " mA";
            description += "\nResolution: " + s.getResolution();

            if (! isSensorImplemented(s))
                description += "\n  --> has not been implemented";

            sensor.put("name", s.getName());
            sensor.put("description", description);

            sensors.add(sensor);
        }

        return sensors;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.capture_mic:
                Intent i = new Intent(this, CaptureMicActivity.class);
                startActivity(i);
                break;
            case R.id.goto_documentation:
                Intent in = new Intent(this, SensorExampleActivity.class);
                startActivity(in);
                break;
            case R.id.goto_source:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pboehm/senseit"));
                startActivity(browserIntent);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overview, menu);
        return true;
    }

    public boolean isSensorImplemented(Sensor s) {
        switch (s.getType()) {
            case Sensor.TYPE_LIGHT:
            case Sensor.TYPE_PROXIMITY:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
            case Sensor.TYPE_RELATIVE_HUMIDITY:
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Sensor s = available_sensors.get(i);

        if (isSensorImplemented(s)) {

            Class cl = null;

            switch (s.getType()) {
                case Sensor.TYPE_LIGHT:
                    cl = LightSensor.class;
                    break;
                case Sensor.TYPE_PROXIMITY:
                    cl = ProximitySensor.class;
                    break;
                case Sensor.TYPE_GRAVITY:
                    cl = GravitySensor.class;
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    cl = AccelerometerSensor.class;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    cl = MagneticSensor.class;
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    cl = GyroscopeSensor.class;
                    break;
                case Sensor.TYPE_PRESSURE:
                    cl = PressureSensor.class;
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    cl = AmbientTemperatureSensor.class;
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    cl = RelativeHumiditySensor.class;
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    cl = LinearAccelerationSensor.class;
                    break;
            }
            if (cl != null) {
                Intent intent = new Intent(this, cl);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sensor has not been implemented",
                    Toast.LENGTH_SHORT).show();
        }
    }

}