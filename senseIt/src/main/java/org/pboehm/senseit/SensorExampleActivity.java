package org.pboehm.senseit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class SensorExampleActivity extends Activity implements SensorEventListener{

    private final String TAG = "senseIt"; // für die Log-Ausgabe verwendet

    private SensorManager manager;
    private Sensor gravitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // wir wollen die Daten eines Gravitationssensors haben
        gravitySensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    /**
     * Wird jeweils dann aufgerufen, wenn die Activity sichtbar wird
     */
    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Wird jeweils dann aufgerufen, wenn die Activity nicht mehr sichtbar ist, also z.B.
     * die App beendet wird oder durch eine andere Activity überdeckt wird
     */
    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    /**
     * Die Methode wird jeweils aufgerufen, wenn der Sensor neue Daten liefert
     *
     * Die Daten stecken jeweils in sensorEvent.values, einem float-Array. Die
     * Anzahl der Werte ist abhängig vom Sensor.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "x = " + sensorEvent.values[0]);
        Log.d(TAG, "y = " + sensorEvent.values[1]);
        Log.d(TAG, "z = " + sensorEvent.values[2]);
    }

    /**
     * Wird aufgerufen, wenn sich die Genauigkeit des entsprechenden Sensors ändert.
     * Wird hier nicht betrachtet.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}