package com.example.android.movieapp;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

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
    private Context context = getContext();
    private DBHandler dbFavourite ; //= new DBHandler(context);
    private GridView gridView;
    private NameListener mListener;
    // public ProgressDialog dialog;

    public void setNameListener(NameListener mListener) {
        this.mListener = mListener;
    }


    public MovieFragment() {   }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Cursor result = dbFavourite.getAllData();
                ArrayList<Movie> favList = new ArrayList<Movie>();
                if(result.getCount() == 0){
                    Toast.makeText(getContext(),"No favourite movies yet!",Toast.LENGTH_LONG).show();
                }
                else{
                    while (result.moveToNext()){
                        int mID = result.getInt(0);
                        String movieTitle = result.getString(1);
                        String moviePoster = result.getString(2);
                        String releaseDate = result.getString(3);
                        String voteAvg = result.getString(4);
                        String overview = result.getString(5);
                        Movie fMov = new Movie();
                        fMov.setmID(mID);
                        fMov.setMoviename(movieTitle);
                        fMov.setMovieposter(moviePoster);
                        fMov.setmReleasedate(releaseDate);
                        fMov.setmVoteAverage(voteAvg);
                        fMov.setmOverview(overview);
                        favList.add(fMov);
                    }
                    mMoviesDetails.add(favList);
                    gridView.setAdapter(mMoviesDetails);
                }


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
        FetchMainMovie MovieView = new FetchMainMovie(mMoviesDetails);
        dbFavourite = new DBHandler(getContext());

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMoviesDetails);


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

        if(isNetworkAvailable()) {

            MovieView.execute("top_rated");}
        else{
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
            //return null;
        }
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
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager)getActivity().getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


}
