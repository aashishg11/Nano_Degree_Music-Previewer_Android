package aashishgodambe.android.MusicPreviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class TopTracksActivity extends ActionBarActivity implements
TopTracksFragment.TracksToMainCommunicatior{

    private static final String TOPTRACKSFRAGMENT_TAG = "TTFTAG";
    private static final String LOGTAG = TopTracksActivity.class.getSimpleName();
    //TopTracksFragment ttf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        // Add the fragment dynamically to the activity as we want this to be seen only when the list item is clicked.


        if (savedInstanceState == null) {
            String artistid = "";
            String artistName = "";
            Intent intent = this.getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                artistid = intent.getStringExtra(intent.EXTRA_TEXT);
                artistName = intent.getStringExtra(TopTracksFragment.ARTISTNAME);
            }

            Bundle args = new Bundle();
            args.putString(TopTracksFragment.ARTIST_ID, artistid);
            args.putString(TopTracksFragment.ARTISTNAME,artistName);

            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.top_tracks_container, fragment, TOPTRACKSFRAGMENT_TAG)
                    .commit();
            Log.d(LOGTAG,"fragment created from toptracks activity");

            try {
                fragment.setTopTracksToMainCommunicator(this);
            } catch (Exception e) {
                Log.e(LOGTAG,"Error in communicating with toptracks fragment.");
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_tracks, menu);
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
    public void respondToSong(String url,String image,String songName,String albumName,String artistName,int pos) {

        Intent playbackIntent = new Intent(this, PlaybackScreenActivity.class)
                .putExtra(PlaybackScreenActivity.SONG_URL, url)
                .putExtra(PlaybackScreenActivity.IMAGE_URL,image)
                .putExtra(PlaybackScreenActivity.SONG_NAME,songName)
                .putExtra(PlaybackScreenActivity.ALBUM_NAME,albumName)
                .putExtra(PlaybackScreenFragment.ARTIST_NAME,artistName)
                .putExtra(PlaybackScreenFragment.TRACK_POSITION,pos);

        startActivity(playbackIntent);
    }
}
