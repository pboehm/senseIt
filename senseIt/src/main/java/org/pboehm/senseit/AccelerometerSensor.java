package org.pboehm.senseit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

public class AccelerometerSensor extends BaseSensorHandler {

    SoundPool pool;
    int soundID;
    long lastPlayedSound = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID = pool.load(this, R.raw.peitsche, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void playSound() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume;

        pool.play(soundID, volume, volume, 1, 0, 1f);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        value1.setText("x = " + sensorEvent.values[0]);
        value2.setText("y = " + sensorEvent.values[1]);
        value3.setText("z = " + sensorEvent.values[2]);

        if (sensorEvent.values[0] > 4.0 && sensorEvent.values[1] > 6.0) {
            if (lastPlayedSound == 0 || ( System.currentTimeMillis() - lastPlayedSound) > 700 ) {
                playSound();
                lastPlayedSound = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected String getSensorTitle() {
        return "Accelerometer Sensor";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }
}