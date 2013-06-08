package org.pboehm.senseit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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

            if (isSensorImplemented(s))
                description += " is implemented";

            sensor.put("name", s.getName());
            sensor.put("description", description);

            sensors.add(sensor);
        }

        return sensors;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.available_sensors:
                Intent i = new Intent(this, AvailableSensorsActivity.class);
                startActivity(i);
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
            }
            if (cl != null) {
                Intent intent = new Intent(this, cl);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sensor is not implemented", Toast.LENGTH_SHORT).show();
        }
    }
}