package aashishgodambe.android.MusicPreviewer;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ArtistsFragment.ArtistToMainComm,
        TopTracksFragment.TracksToMainCommunicatior {


    private static final String TOPTRACKSFRAGMENT_TAG = "TTFTAG";
    private static final String LOGTAG = MainActivity.class.getSimpleName();
    boolean mtwopane;
    ArtistsFragment af;
    TopTracksFragment ttf;

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getFragmentManager();

        af = (ArtistsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_artist);
        af.setArtistToMainComm(this);


        if (findViewById(R.id.top_tracks_container) != null) {
            mtwopane = true;

        } else {
            mtwopane = false;
        }

    }

    @Override
    public void respondToArtist(String id, String name) {

        if (mtwopane) {
            Bundle args = new Bundle();
            args.putString(TopTracksFragment.ARTIST_ID, id);
            args.putString(TopTracksFragment.ARTISTNAME, name);
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment, TOPTRACKSFRAGMENT_TAG)
                    .commit();
            Log.d(LOGTAG, "Fragment created from main activity");

            try {
                fragment.setTopTracksToMainCommunicator(this);
            } catch (Exception e) {
                Log.e(LOGTAG, "Error in communicating with toptracks fragment.");
            }


        } else {
            //Launch new activity when a list item is pressed and passes the artist id.
            Intent topTrackIntent = new Intent(this, TopTracksActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, id)
                    .putExtra(TopTracksFragment.ARTISTNAME, name);
            startActivity(topTrackIntent);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void respondToSong(String url, String image, String songName, String albumName,String artistName,int pos) {

        if (mtwopane) {

            Bundle args = new Bundle();
            args.putString(PlaybackScreenFragment.SONG_URL, url);
            args.putString(PlaybackScreenFragment.IMAGE_URL, image);
            args.putString(PlaybackScreenFragment.SONG_NAME, songName);
            args.putString(PlaybackScreenFragment.ALBUM_NAME, albumName);
            args.putString(PlaybackScreenFragment.ARTIST_NAME,artistName);
            args.putInt(PlaybackScreenFragment.TRACK_POSITION,pos);
            PlaybackScreenFragment fragment = new PlaybackScreenFragment();
            fragment.setArguments(args);
            FragmentManager manager = getFragmentManager();
            fragment.show(manager, "Playback Fragment");


        }

    }
}

