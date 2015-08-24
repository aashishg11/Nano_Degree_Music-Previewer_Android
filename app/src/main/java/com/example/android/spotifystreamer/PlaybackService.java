package com.example.android.spotifystreamer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Aashish on 8/7/2015.
 */
public class PlaybackService extends Service {

    public static String ACTION_PLAY = "com.example.android.spotifystreamer.PLAY";
    public static String ACTION_PAUSE = "com.example.android.spotifystreamer.PAUSE";

    private boolean startPlaying = false;

    private MediaPlayer mMediaPlayer = null;
   // private final Handler handler = new Handler();
    private Intent intent;
    private BroadcastReceiver receiver;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        //intent =

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String intentAction = intent.getAction();

                if(intentAction.equals(ACTION_PLAY)){
                    if(mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    }
                    else if(mMediaPlayer == null) {
                        startPlaying = true;
                    }
                }
                else if(intentAction.equals(ACTION_PAUSE)){
                    if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }

            }
        };
        registerReceiver(receiver, filter);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            String url = intent.getStringExtra("previewUrl");
           // handler.removeCallbacks(sendUpdatesToUI);
            //handler.postDelayed(sendUpdatesToUI, 200);

            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                       // sendMax();
                        if(startPlaying)
                            mMediaPlayer.start();
                    }
                });
                mMediaPlayer.prepareAsync();

            } catch (IOException e) {
                Log.w("IO Exception: ", e.toString());
            }
        }

        return START_STICKY;
    }

    public void onDestroy() {
        /* clean up */
        Log.i("service","destroying service");
        //handler.removeCallbacks(sendUpdatesToUI);
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();

        /* remove our receiver */
        unregisterReceiver(receiver);
    }

    public void onCompletion(MediaPlayer _mediaPlayer) {
        stopSelf();
    }
}
