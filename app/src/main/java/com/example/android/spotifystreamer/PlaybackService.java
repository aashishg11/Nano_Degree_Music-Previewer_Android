package com.example.android.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Aashish on 8/7/2015.
 */
public class PlaybackService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
