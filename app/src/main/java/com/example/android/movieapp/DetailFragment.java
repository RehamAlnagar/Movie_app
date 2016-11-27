package com.example.android.movieapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class DetailFragment extends android.support.v4.app.Fragment {

    private Context mContext;
    Movie mMovieSelected = new Movie();
    DBHandler dbHandler;
    protected ArrayAdapter mMoviestrailers;
    protected ArrayAdapter mMoviesReviews;
    protected ArrayList<String> arrayTrailers = new ArrayList<String>();
    protected ArrayList<String> arrayReviews = new ArrayList<String>();


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        mMoviestrailers = new ArrayAdapter(getContext()
                ,R.layout.list_item_trailers,R.id.list_item_trailer,new ArrayList());
        FetchTrailers movieView = new FetchTrailers(mMoviestrailers);

        mMoviesReviews = new ArrayAdapter(getContext(),R.layout.list_item_review,R.id.reviews_item,new ArrayList());
        FetchReviews movieReview = new FetchReviews(mMoviesReviews);

        dbHandler = new DBHandler(getContext());

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView Title = (TextView) rootView.findViewById(R.id.detail_Title);
        ImageView Poster = (ImageView) rootView.findViewById(R.id.detail_poster);
        TextView date = (TextView) rootView.findViewById(R.id.moviedate);
        TextView rate = (TextView) rootView.findViewById(R.id.movierate);
        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        Button btn = (Button) rootView.findViewById(R.id.add_favbtn);

        Bundle bundle = getArguments();

        int MovieId = bundle.getInt("ID");
        String MovieName = bundle.getString("Name");
        String MoviePoster = bundle.getString("Poster");
        String MovieDate = bundle.getString("ReleaseDate");
        String MovieVoteAvg = bundle.getString("voteAverage");
        String MovieOverview = bundle.getString("Overview");

        Title.setText(MovieName);
        date.setText(MovieDate);
        rate.setText(MovieVoteAvg);
        overview.setText(MovieOverview);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + MoviePoster).into(Poster);

        mMovieSelected.setmID(MovieId);
        mMovieSelected.setMoviename(MovieName);
        mMovieSelected.setMovieposter(MoviePoster);
        mMovieSelected.setmReleasedate(MovieDate);
        mMovieSelected.setmVoteAverage(MovieVoteAvg);
        mMovieSelected.setmOverview(MovieOverview);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.addMovies(mMovieSelected);
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.trailers_list);
        listView.setAdapter(mMoviestrailers);

        ListView listViewReviews = (ListView) rootView.findViewById(R.id.reviews_list);
        listViewReviews.setAdapter(mMoviesReviews);

        movieView.execute(String.valueOf(mMovieSelected.getmID()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String moviekey = arrayTrailers.get(0);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(moviekey)));
            }
        });

        movieReview.execute(String.valueOf(mMovieSelected.getmID()));

        return rootView;
    }

    public class FetchTrailers extends AsyncTask<String, Void, ArrayList<String>>{
        private final String LOG_TAG = FetchTrailers.class.getSimpleName();

        public FetchTrailers(ArrayAdapter<String> arrayAdapter){
            mMoviestrailers = arrayAdapter;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String MovieJsonStr = null;
            final String Movie_BASE_URL ="https://api.themoviedb.org/3/movie/";
            try {
                URL url = new URL(Movie_BASE_URL + params[0] + "/videos?api_key=3d03f6c2413726779fb4dcd3135aa7bb&language=en-US");

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
                arrayTrailers.clear();
                for (int i = 0; i < result.length(); i++){
                    String moviedata = "";
                    JSONObject jsonObject = result.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    moviedata = "https://www.youtube.com/watch?v=" +key;
                    arrayTrailers.add(moviedata);
                }

                Log.v(LOG_TAG, "Movie string: " + MovieJsonStr);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
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
            return arrayTrailers;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                mMoviestrailers.clear();
                for (String movieStr : result) {
                    mMoviestrailers.add(movieStr);
                }
            }
        }
    }

    public class FetchReviews extends AsyncTask<String, Void, ArrayList<String>>{

        public FetchReviews(ArrayAdapter<String> arrayAdapter){
            mMoviesReviews = arrayAdapter;
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String MovieJsonStr = null;
            final String Movie_BASE_URL ="https://api.themoviedb.org/3/movie/";
            try {
                URL url = new URL(Movie_BASE_URL + params[0] + "/reviews?api_key=3d03f6c2413726779fb4dcd3135aa7bb&language=en-US");
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
                arrayReviews.clear();
                for (int i = 0; i < result.length(); i++){
                    String moviedata = "";
                    JSONObject jsonObject = result.getJSONObject(i);
                    String author = jsonObject.getString("author");
                    String content = jsonObject.getString("content");

                    moviedata = "Author: " + author +"\n"+"Content: "+ content +"\n\n";
                    arrayReviews.add(moviedata);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            return arrayReviews;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                mMoviesReviews.clear();
                for (String movieStr : result) {
                    mMoviesReviews.add(movieStr);
                }
            }
        }
    }

}