package com.example.android.movieapp;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

       if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new DetailFragment())
                    .commit();
        }

    }

   public  class DetailFragment extends Fragment {
       // private  final String LOG_TAG = DetailFragment.class.getSimpleName();

       private Context mContext;
        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView Title = (TextView) rootView.findViewById(R.id.detail_Title);
            ImageView Poster =(ImageView) rootView.findViewById(R.id.detail_poster);
            TextView date = (TextView) rootView.findViewById(R.id.moviedate);
            TextView rate = (TextView) rootView.findViewById(R.id.movierate);
            TextView overview = (TextView) rootView.findViewById(R.id.overview);

            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();

            String MovieName =  bundle.getString("Name");
            String MoviePoster = bundle.getString("Poster");
            String MovieDate = bundle.getString("ReleaseDate");
            String MovieRate = bundle.getString("voteAverage");
            String MovieOverview = bundle.getString("Overview");

           //if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
               Title.setText(MovieName);
               date.setText(MovieDate);
               rate.setText(MovieRate);
               overview.setText(MovieOverview);
               Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + MoviePoster).into(Poster);

           //}
           /* Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context,MovieName, duration);
           toast.show();*/


            return rootView;

        }

    }

}
