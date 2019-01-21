package aashishgodambe.android.MusicPreviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class PlaybackScreenActivity extends AppCompatActivity {
    static final String SONG_URL = "URL";
    static final String IMAGE_URL = "IMAGE";
    static final String ALBUM_NAME = "ALBUM";
    static final String SONG_NAME = "SONG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_screen);

        if (savedInstanceState == null) {
            String url = "";
            String trackName = "";
            String image = "";
            String albumName = "";
            /*Intent intent = this.getIntent();
            if (intent != null) {
                url = intent.getStringExtra(SONG_URL);
                Log.d("PlaybackActivity",url);
                trackName = intent.getStringExtra(SONG_NAME);
                image = intent.getStringExtra(IMAGE_URL);
                albumName = intent.getStringExtra(ALBUM_NAME);

            }*/

            /*Bundle args = new Bundle();
            args.putString(PlaybackScreenFragment.SONG_URL, url);
            args.putString(PlaybackScreenFragment.IMAGE_URL,image);
            args.putString(PlaybackScreenFragment.SONG_NAME,trackName);
            args.putString(PlaybackScreenFragment.ALBUM_NAME,albumName);
            PlaybackScreenFragment fragment = new PlaybackScreenFragment();
            fragment.setArguments(args);*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playback_screen, menu);
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
}
