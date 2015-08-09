package com.example.android.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksFragment extends Fragment {

    private static final String LOG_TAG = TopTracksFragment.class.getSimpleName();
    private static final String STATE_TOP_TRACKS = "state_top_tracks";
    // Artist id to get relevant track information.
    private String artistId;
String artistName;
    static final String ARTIST_ID = "ID";
    static final String ARTISTNAME = "NAME";
    // Arraylist to store track information
    public static ArrayList<TopTrackInfo> allTracks = new ArrayList<>();
    ListView listView;
    TracksToMainCommunicatior communicatior;
    //TopTracksToMainComm topTracksToMainComm;

    public TopTracksFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_TOP_TRACKS, allTracks);
        outState.putString("Artist_name",artistName);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            artistId = arguments.getString(TopTracksFragment.ARTIST_ID);
            artistName = arguments.getString(TopTracksFragment.ARTISTNAME);
            Log.d(LOG_TAG,artistName);
        }

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        // Get a reference to the listview
        listView = (ListView) rootView.findViewById(R.id.listview_toptracks);

        // Intent for receiving the artist id.
//        Intent intent = getActivity().getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            artistId = intent.getStringExtra(intent.EXTRA_TEXT);
//            Log.v(LOG_TAG, "Id is " + artistId);
//
//        }

        if (savedInstanceState != null) {
            allTracks = savedInstanceState.getParcelableArrayList(STATE_TOP_TRACKS);
            artistName = savedInstanceState.getString("Artist_name");
            Log.v(LOG_TAG,artistName);
            listView.setAdapter(new TrackAdapter(getActivity(), allTracks));
        } else {
            FetchTopTracks fetchTopTracks = new FetchTopTracks();
            fetchTopTracks.execute(artistId);
            Log.v(LOG_TAG, "Track size = " + allTracks.size());
        }

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String url = allTracks.get(position).previewUrl;
                        String image = allTracks.get(position).imageUrl_large;
                        String songName = allTracks.get(position).trackName;
                        String albumName = allTracks.get(position).albumName;
                        int pos = position;
                        communicatior.respondToSong(url,image,songName,albumName,artistName,pos);
                    }
                });

        return rootView;

    }

    public void setTopTracksToMainCommunicator(TracksToMainCommunicatior communicator){
        this.communicatior = communicator;
    }

    public interface TracksToMainCommunicatior{

        public void respondToSong(String url,String image,String songName,String albumName,String artistName,int pos);

    }

    // A class which holds information regarding every track.
    public static class TopTrackInfo implements Parcelable {
        String trackName;
        String albumName;
        String imageUrl_small;
        String imageUrl_large;
        String previewUrl;
        int topPopularity;


        public TopTrackInfo(Parcel input) {
            trackName = input.readString();
            albumName = input.readString();
            imageUrl_small = input.readString();
            imageUrl_large = input.readString();
            previewUrl = input.readString();
            topPopularity = input.readInt();
        }

        public TopTrackInfo(String trackName, String albumName, String imageUrl_small, String imageUrl_large, String previewUrl
                , int topPopularity) {
            this.albumName = albumName;
            this.trackName = trackName;
            this.imageUrl_small = imageUrl_small;
            this.imageUrl_large = imageUrl_large;
            this.previewUrl = previewUrl;
            this.topPopularity = topPopularity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(trackName);
            dest.writeString(albumName);
            dest.writeString(imageUrl_small);
            dest.writeString(imageUrl_large);
            dest.writeString(previewUrl);
            dest.writeInt(topPopularity);

        }

        public static final Parcelable.Creator<TopTrackInfo> CREATOR
                = new Parcelable.Creator<TopTrackInfo>() {
            public TopTrackInfo createFromParcel(Parcel in) {
                return new TopTrackInfo(in);
            }

            public TopTrackInfo[] newArray(int size) {
                return new TopTrackInfo[size];
            }
        };
    }

    public class TrackAdapter extends BaseAdapter {

        Context context;
        ArrayList<TopTrackInfo> allTracks;

        TrackAdapter(Context c, ArrayList<TopTrackInfo> allTracks) {
            context = c;
            this.allTracks = allTracks;

        }

        @Override
        public int getCount() {
            return allTracks.size();
        }

        @Override
        public Object getItem(int position) {
            return allTracks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item_top_artist, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                Log.d(LOG_TAG, "Top track item created");
            } else {
                holder = (ViewHolder) row.getTag();
                Log.d(LOG_TAG, "Top track item recycled");
            }
            holder.textViewSong.setText(allTracks.get(position).trackName);
            holder.textViewAlbum.setText(allTracks.get(position).albumName);


            String iconlink = allTracks.get(position).imageUrl_small;
            if (!iconlink.equals("")) {

                Picasso.with(context).load(iconlink).into(holder.imageView);
            }

            return row;
        }

        // A viewholder used to create reference to xml objects
        class ViewHolder {
            TextView textViewSong;
            TextView textViewAlbum;
            ImageView imageView;

            ViewHolder(View view) {
                textViewSong = (TextView) view.findViewById(R.id.textView_top_song);
                textViewAlbum = (TextView) view.findViewById(R.id.textView_top_album);
                imageView = (ImageView) view.findViewById(R.id.imageView_top_artist);
            }

        }
    }

    public class FetchTopTracks extends AsyncTask<String, Void, ArrayList<TopTrackInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allTracks.clear();
        }

        @Override
        protected ArrayList<TopTrackInfo> doInBackground(String... params) {

            try {

                SpotifyApi spotifyApi = new SpotifyApi();
                SpotifyService spotifyService = spotifyApi.getService();
                Map<String, Object> options = new HashMap<>();
                options.put("country", "US");
                Tracks track = spotifyService.getArtistTopTrack(params[0], options);
                Log.v(LOG_TAG, "No of tracks: " + track.tracks.size());

                // Extract information of every track and store in TopTrackInfo object.
                for (int i = 0; i < track.tracks.size(); i++) {
                    String trackName = track.tracks.get(i).name;
                    String albumName = track.tracks.get(i).album.name;
                    String previewUrl = track.tracks.get(i).preview_url;
                    int topPopularity = track.tracks.get(i).popularity;
                    String imageUrl_small = findImageUrlSmall(track.tracks.get(i).album.images);
                    String imageUrl_large = findImageUrlLarge(track.tracks.get(i).album.images);
                    TopTrackInfo topTrackInfo = new TopTrackInfo(trackName
                            , albumName
                            , imageUrl_small
                            , imageUrl_large
                            , previewUrl
                            , topPopularity);

                    allTracks.add(topTrackInfo);


                }
                Log.v(LOG_TAG, "No of tracks: " + allTracks.size());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            }

            return allTracks;
        }

        // Finds the URL for small image
        public String findImageUrlSmall(List<Image> imagesList) {
            String str = "";
            for (int i = 0; i < imagesList.size(); i++) {
                Image image = imagesList.get(i);
                if (image.height < 80 && image.width < 80) {
                    str = image.url;
                }
            }

            return str;
        }

        // Finds the URL for large image
        public String findImageUrlLarge(List<Image> imagesList) {
            String str = "";
            for (int i = 0; i < imagesList.size(); i++) {
                Image image = imagesList.get(i);
                if (image.height > 600 && image.width > 600) {
                    str = image.url;
                }
            }

            return str;
        }

        @Override
        protected void onPostExecute(ArrayList<TopTrackInfo> topTrackInfos) {
            super.onPostExecute(topTrackInfos);

            // Display toast if tracks are not available.
            if (allTracks.size() == 0) {
                Toast.makeText(getActivity(), "Top tracks not available", Toast.LENGTH_SHORT).show();
            } else {
                Log.v(LOG_TAG, "No of tracks on post: " + allTracks.size());
                listView.setAdapter(new TrackAdapter(getActivity(), allTracks));
            }
        }
    }


}
