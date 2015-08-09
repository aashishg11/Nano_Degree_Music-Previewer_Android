package com.example.android.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Aashish on 8/7/2015.
 */
public class PlaybackService extends Service {

    public static String ACTION_PLAY = "com.example.android.spotifystreamer.PLAY";
    public static String ACTION_PAUSE = "com.example.android.spotifystreamer.PAUSE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
