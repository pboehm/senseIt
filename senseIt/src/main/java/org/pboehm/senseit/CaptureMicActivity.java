package org.pboehm.senseit;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

public class CaptureMicActivity extends Activity {

    AudioRecorder recorder;
    String audioFile;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioFile = "mic_record.3gp";
        recorder = new AudioRecorder(audioFile);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            recorder.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}