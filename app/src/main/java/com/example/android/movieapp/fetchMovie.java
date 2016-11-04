/*package com.example.android.movieapp;


import android.os.AsyncTask;
import android.util.Log;

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

public class fetchMovie extends AsyncTask<String,Void,ArrayList<Movie>>
{
    private final String LOG_TAG = fetchMovie.class.getSimpleName();

    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

    private customAdapter mMovieAdapter;


    public fetchMovie( customAdapter ada){

        mMovieAdapter = ada;
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
                URL url = new URL(Movie_BASE_URL + params[0]+"?api_key=3d03f6c2413726779fb4dcd3135aa7bb");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MovieJsonStr = buffer.toString();
                JSONObject movieJason = new JSONObject(MovieJsonStr);
                JSONArray result = movieJason.getJSONArray("results");
                for (int i=0;i<result.length();i++){
                     Movie movieData = new Movie() ;
                    JSONObject jsonObject = result.getJSONObject(i);
                    String poster = jsonObject.getString("poster_path");
                    String title = jsonObject.getString("original_title");
                    movieData.setMoviename(title);
                    movieData.setMovieposter(poster);
                    movieArrayList.add(movieData);

                }

                Log.v(LOG_TAG, "Movie string: " + MovieJsonStr);
                return movieArrayList;


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error "+ e.toString());
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


        return movieArrayList;
    }




    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        if(movies != null){
            movieArrayList.clear();
            mMovieAdapter.add(movies);}
        mMovieAdapter.notifyDataSetChanged();

    }
}
*/