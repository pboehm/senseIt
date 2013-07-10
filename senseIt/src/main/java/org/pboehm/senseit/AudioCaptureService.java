package org.pboehm.senseit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class AudioCaptureService extends Service {
    public static final String AUDIO_FILENAME = "mic_record.3gp";
    private AudioRecorder recorder;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            recorder = new AudioRecorder(AUDIO_FILENAME);
            recorder.start();

            Toast.makeText(this, "Recording has started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            recorder.stop();
            Toast.makeText(this, "Recording has stopped", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
