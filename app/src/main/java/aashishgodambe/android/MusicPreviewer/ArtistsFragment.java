package aashishgodambe.android.MusicPreviewer;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistsFragment extends Fragment {

    private static final String STATE_ARTIST_INFO = "state_artist_info";
    // Array which holds information of all artist
    ArrayList<ArtistInfo> allArtists = new ArrayList<ArtistInfo>();
    ListView listView;
    ArtistAdapter artistAdapter;
    ArtistToMainComm artistToMainComm;

    public ArtistsFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_ARTIST_INFO, allArtists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);

        // Get a reference to the listview
        listView = (ListView) rootView.findViewById(R.id.listview_artists);

        if (savedInstanceState != null) {

            allArtists = savedInstanceState.getParcelableArrayList(STATE_ARTIST_INFO);
            artistAdapter = new ArtistAdapter(getActivity(), allArtists);
            listView.setAdapter(artistAdapter);

        } else {
            final SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);

            searchView.setQueryHint("Enter artist name");

            searchView.setOnQueryTextListener(
                    new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {

                            FetchAlbumId fetchAlbumId = new FetchAlbumId();
                            fetchAlbumId.execute(query + "*");

                            artistAdapter = new ArtistAdapter(getActivity(), allArtists);
                            listView.setAdapter(artistAdapter);
                            Log.d("Artist Frag", "Adapter set");
                            //Close the virtual keyboard when submit is pressed.
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            return true;
                        }
                    }
            );
        }
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String artistId = allArtists.get(position).artistId;
                        String artistName = allArtists.get(position).artistName;
                        artistToMainComm.respondToArtist(artistId,artistName);

                    }
                }

        );

        return rootView;

    }

    public void setArtistToMainComm(ArtistToMainComm artistToMainComm) {
        this.artistToMainComm = artistToMainComm;
    }

    public interface ArtistToMainComm {

        public void respondToArtist(String id,String name);

    }

    // A class to store the info of each artist
    public static class ArtistInfo implements Parcelable {
        String artistName;
        String artistId;
        String imageUrl;
        int popularity;

        public ArtistInfo(Parcel input) {
            artistName = input.readString();
            artistId = input.readString();
            imageUrl = input.readString();
            popularity = input.readInt();

        }

        public ArtistInfo(String name, String id, String imageUrl, int popularity) {
            artistName = name;
            artistId = id;
            this.popularity = popularity;
            this.imageUrl = imageUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(artistName);
            dest.writeString(artistId);
            dest.writeString(imageUrl);
            dest.writeInt(popularity);

        }

        public static final Parcelable.Creator<ArtistInfo> CREATOR
                = new Parcelable.Creator<ArtistInfo>() {
            public ArtistInfo createFromParcel(Parcel in) {
                return new ArtistInfo(in);
            }

            public ArtistInfo[] newArray(int size) {
                return new ArtistInfo[size];
            }
        };

    }

    class ArtistAdapter extends BaseAdapter {

        Context context;
        ArrayList<ArtistInfo> allArtists;

        ArtistAdapter(Context c, ArrayList<ArtistInfo> allArtists) {
            context = c;
            this.allArtists = allArtists;

        }

        @Override

        public int getCount() {
            return allArtists.size();
        }

        @Override
        public Object getItem(int position) {
            return allArtists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder holder = null;

            // If the list item row is created for first time, perform inflation and referencing.
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item_artists, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                Log.d("Row", "Creating new row");
            }
            // If the row was already created, perform recycling for better performance optimization.
            else {
                holder = (ViewHolder) row.getTag();
                Log.d("ROW", "Recycling row");
            }
            holder.textViewName.setText(allArtists.get(position).artistName);
            holder.textViewPopularity.setText(allArtists.get(position).popularity + "");

            String iconlink = allArtists.get(position).imageUrl;
            if (!iconlink.equals("")) {
                Picasso.with(context).load(iconlink).into(holder.imageView);
            }

            return row;
        }

        // A viewholder used to create reference to xml objects.
        class ViewHolder {
            TextView textViewName;
            TextView textViewPopularity;
            ImageView imageView;

            ViewHolder(View rowView) {
                textViewName = (TextView) rowView.findViewById(R.id.textview_artist_name);
                textViewPopularity = (TextView) rowView.findViewById(R.id.textview_pop);
                imageView = (ImageView) rowView.findViewById(R.id.imageView);
            }

        }

    }

    public class FetchAlbumId extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchAlbumId.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allArtists.clear();
        }


        @Override
        protected Void doInBackground(String... params) {

            ArtistsPager result;
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                result = spotify.searchArtists(params[0]);

                String artistName = "";
                int sizeOfArtist = result.artists.items.size();

                // Extract information of every artist and store in ArtistInfo object.
                for (int i = 0; i < sizeOfArtist; i++) {

                    Artist artist = result.artists.items.get(i);
                    String name = artist.name;
                    String id = artist.id;
                    int popularity = artist.popularity;
                    String imageurl = findImageUrl(artist.images);
                    ArtistInfo artistInfo = new ArtistInfo(name, id, imageurl, popularity);
                    allArtists.add(artistInfo);

                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            }


            return null;
        }

        // Finds the image from the list of images whose size is less than 80 x 80
        public String findImageUrl(List<Image> imagesList) {
            String str = "";
            for (int i = 0; i < imagesList.size(); i++) {
                Image image = imagesList.get(i);
                if (image.height < 80 && image.width < 80) {
                    str = image.url;
                }
            }

            return str;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            artistAdapter.notifyDataSetChanged();

            if (allArtists.size() == 0) {
                Toast.makeText(getActivity(), "No matches found. Please refine your search.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}