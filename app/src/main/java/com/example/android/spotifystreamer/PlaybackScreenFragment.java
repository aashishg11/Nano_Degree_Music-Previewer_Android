package com.example.android.spotifystreamer;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaybackScreenFragment extends DialogFragment {

    TextView songTX;
    TextView albumTX;
    TextView artistTX;
    ImageView imageView;
    String url = "";
    String trackName;
    String image = "";
    String albumName;
    String artistName;
    static final String SONG_URL = "URL";
    static final String IMAGE_URL = "IMAGE";
    static final String ARTIST_NAME = "NAME";
    static final String ALBUM_NAME = "ALBUM";
    static final String SONG_NAME = "SONG";

    public PlaybackScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playback_screen, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            url = intent.getStringExtra(SONG_URL);
            trackName = intent.getStringExtra(SONG_NAME);
            artistName = intent.getStringExtra(ARTIST_NAME);
            albumName = intent.getStringExtra(ALBUM_NAME);
            image = intent.getStringExtra(IMAGE_URL);

        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            url = arguments.getString(PlaybackScreenFragment.SONG_URL);
            image = arguments.getString(PlaybackScreenFragment.IMAGE_URL);
            artistName = arguments.getString(PlaybackScreenFragment.ARTIST_NAME);
            trackName = arguments.getString(PlaybackScreenFragment.SONG_NAME);
            albumName = arguments.getString(PlaybackScreenFragment.ALBUM_NAME);
        }


        artistTX = (TextView) view.findViewById(R.id.textview_playback_artist);
        artistTX.setText(artistName);
        songTX = (TextView) view.findViewById(R.id.textview_playback_song);
        songTX.setText(trackName);
        albumTX = (TextView) view.findViewById(R.id.textview_playback_album);
        albumTX.setText(albumName);
        if (!image.equals("")) {
            imageView = (ImageView) view.findViewById(R.id.imageview_playback);
            Picasso.with(getActivity()).load(image).into(imageView);
        }
        return view;
    }

}
