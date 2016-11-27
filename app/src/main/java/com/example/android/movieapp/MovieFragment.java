package com.example.android.movieapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MovieFragment extends Fragment {


    protected customAdapter mMoviesDetails;
    protected ArrayList<Movie> arrayData = new ArrayList<Movie>();
    private Context context;
    private NameListener mListener;
    public ProgressDialog dialog;

    public void setNameListener(NameListener mListener) {
        this.mListener = mListener;
    }


    public MovieFragment() {   }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        FetchMainMovie MovieView = new FetchMainMovie(mMoviesDetails) ;
        switch (id){
            case R.id.top_rated :
                MovieView.execute("top_rated");
                return true;
            case R.id.most_popular:
                MovieView.execute("popular");
                return true;
            case R.id.favourite:


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMoviesDetails =
                new customAdapter(getContext(), new ArrayList<Movie>());
        FetchMainMovie MovieView = new FetchMainMovie(mMoviesDetails) ;


        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // Get a reference to the gridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMoviesDetails);

        MovieView.execute("top_rated");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private Context applicationContext;

            public Context getApplicationContext() {
                return applicationContext;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie moviedetail = (Movie) mMoviesDetails.getItem(position);
                mListener.setSelectedName(moviedetail);
            }
        });

        return rootView;
    }


    public class FetchMainMovie extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMainMovie.class.getSimpleName();


        public FetchMainMovie() {
        }

        public FetchMainMovie(customAdapter ada) {

            mMoviesDetails = ada;
        }


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String MovieJsonStr = null;

            final String Movie_BASE_URL = "https://api.themoviedb.org/3/movie/";

            try {
                URL url = new URL(Movie_BASE_URL + params[0] + "?api_key=3d03f6c2413726779fb4dcd3135aa7bb");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                MovieJsonStr = buffer.toString();
                JSONObject movieJason = new JSONObject(MovieJsonStr);
                JSONArray result = movieJason.getJSONArray("results");
                arrayData.clear();
                for (int i = 0; i < result.length(); i++) {
                    Movie movieData = new Movie();
                    JSONObject jsonObject = result.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String poster = jsonObject.getString("poster_path");
                    String title = jsonObject.getString("original_title");
                    String releaseDate = jsonObject.getString("release_date");
                    String overview = jsonObject.getString("overview");
                    String average = jsonObject.getString("vote_average");

                    movieData.setmID(id);
                    movieData.setMoviename(title);
                    movieData.setMovieposter(poster);
                    movieData.setmReleasedate(releaseDate);
                    movieData.setmOverview(overview);
                    movieData.setmVoteAverage(average);
                    arrayData.add(movieData);
                }

                Log.v(LOG_TAG, "Movie string: " + MovieJsonStr);
                return arrayData;


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e.toString());
                // If the code didn't successfully get the weather data, there's no point in attemping

                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return arrayData;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null) {
                mMoviesDetails.add(movies);
            }
            mMoviesDetails.notifyDataSetChanged();

        }
    }

}
