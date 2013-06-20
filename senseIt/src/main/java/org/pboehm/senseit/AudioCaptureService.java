package org.pboehm.senseit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AudioCaptureService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
}
