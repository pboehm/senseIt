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
    float lastX = 0.0F, lastY = 0.0F, lastZ = 0.0F;
    private final float NOISE = (float) 4.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID = pool.load(this, R.raw.peitsche, 1);
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
        float x,y,z;

        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];

        value1.setText("x = " + x);
        value2.setText("y = " + y);
        value3.setText("z = " + z);

        // play a nice sound if we shake we phone in x direction
        if (Math.abs(lastX - x) > NOISE && y > 7.0) {
            if (lastPlayedSound == 0 || ( System.currentTimeMillis() - lastPlayedSound) > 700 ) {
                playSound();
                lastPlayedSound = System.currentTimeMillis();
            }
        }

        lastX = x;
        lastY = y;
        lastZ = z;
    }

    @Override
    protected String getSensorTitle() {
        return "Accelerometer";
    }

    @Override
    protected int getSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    protected String getUnit() {
        return "m/s\u00B2";
    }
}