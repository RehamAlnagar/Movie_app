package com.example.android.movieapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    protected TrailerAdapter mMoviestrailers;
    protected ArrayAdapter mMoviesReviews;
    protected ArrayList<String> arrayTrailers = new ArrayList<String>();
    protected ArrayList<String> arrayReviews = new ArrayList<String>();

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    public static void updateListview(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        mMoviestrailers = new TrailerAdapter(getContext(),new ArrayList<String>());
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
        Button favbtn = (Button) rootView.findViewById(R.id.add_favbtn);
        Button delbtn = (Button) rootView.findViewById(R.id.delete_btn);

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

        favbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.addMovies(mMovieSelected);
            }
        });
        delbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Integer deletedRow = dbHandler.deleteMovies(String.valueOf(mMovieSelected.getmID()));
                if(deletedRow > 0){
                   Toast.makeText(getContext(), "Movie Removed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Movie has already been removed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.trailers_list);
        listView.setAdapter(mMoviestrailers);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        updateListview(listView);


        ListView listViewReviews = (ListView) rootView.findViewById(R.id.reviews_list);
        listViewReviews.setAdapter(mMoviesReviews);

        updateListview(listViewReviews);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String moviekey = (String) mMoviestrailers.getItem(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(moviekey)));
            }
        });

        if(isNetworkAvailable()) {
            movieView.execute(String.valueOf(mMovieSelected.getmID()));
            movieReview.execute(String.valueOf(mMovieSelected.getmID()));
        }

        else{
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public class FetchTrailers extends AsyncTask<String, Void, ArrayList<String>>{
        private final String LOG_TAG = FetchTrailers.class.getSimpleName();

        public FetchTrailers(TrailerAdapter Adapter){
            mMoviestrailers = Adapter;
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
                URL url = new URL(Movie_BASE_URL + params[0] + "/videos?api_key=&language=en-US"); //add api key

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
            super.onPostExecute(result);
            if (result != null) {
                mMoviestrailers.add(result);
            }
            mMoviestrailers.notifyDataSetChanged();
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
                URL url = new URL(Movie_BASE_URL + params[0] + "/reviews?api_key=&language=en-US"); //add api key
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

                    moviedata = "=> Author: " + author +"\n"+"Content: "+ content +"\n\n";
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
            super.onPostExecute(result);
            if (result != null) {
                mMoviesReviews.clear();
                for (String movieStr : result) {
                    mMoviesReviews.add(movieStr);
                }
            }
            mMoviesReviews.notifyDataSetChanged();

        }
    }

}