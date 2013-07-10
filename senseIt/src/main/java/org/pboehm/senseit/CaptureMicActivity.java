package org.pboehm.senseit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class CaptureMicActivity extends Activity implements View.OnClickListener {

    Button start, stop, play;
    String audioFile;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);

        start = (Button) findViewById(R.id.btn_start_capturing);
        start.setOnClickListener(this);

        stop  = (Button) findViewById(R.id.btn_stop_capturing);
        stop.setOnClickListener(this);

        play  = (Button) findViewById(R.id.btn_play_captured_sound);
        play.setOnClickListener(this);

        audioFile = AudioRecorder.getAudioPath(AudioCaptureService.AUDIO_FILENAME);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isCaptureServiceRunning()) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
        }

        if (new File(audioFile).exists()) {
            play.setEnabled(true);
        } else {
            play.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_capturing:
                if (! isCaptureServiceRunning()) {
                    startService(new Intent(this, AudioCaptureService.class));
                    start.setEnabled(false);
                    stop.setEnabled(true);
                }
                break;
            case R.id.btn_stop_capturing:
                if (isCaptureServiceRunning()) {
                    stopService(new Intent(this, AudioCaptureService.class));
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                }
                break;
            case R.id.btn_play_captured_sound:
                Intent audioIntent = new Intent().setAction(
                    Intent.ACTION_VIEW).setDataAndType(
                        Uri.fromFile(new File(audioFile)), "audio/*");
                startActivity(audioIntent);
                break;
        }
    }

    private boolean isCaptureServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AudioCaptureService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}