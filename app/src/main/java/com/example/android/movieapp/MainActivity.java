package com.example.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NameListener {
    // private Context context;
    boolean mInTwoPane = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // inflate the main activity
        MovieFragment mMovieFragment = new MovieFragment();
        mMovieFragment.setNameListener(this);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame1, mMovieFragment).commit();
        }
        //check if two pane
        if (null != findViewById(R.id.frame_details)) {
            mInTwoPane = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


   @Override
    public void setSelectedName(Movie moviedetail) {
        //Case one pane
        if (!mInTwoPane) {

            Intent intent = new Intent(this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID",moviedetail.getmID());
            bundle.putString("Name", moviedetail.getMoviename());
            bundle.putString("Poster", moviedetail.getMovieposter());
            bundle.putString("ReleaseDate", moviedetail.getmReleasedate());
            bundle.putString("Overview", moviedetail.getmOverview());
            bundle.putString("voteAverage", moviedetail.getmVoteAverage());

            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            //Case two pane
            DetailFragment mDetailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("ID",moviedetail.getmID());
            bundle.putString("Name", moviedetail.getMoviename());
            bundle.putString("Poster", moviedetail.getMovieposter());
            bundle.putString("ReleaseDate", moviedetail.getmReleasedate());
            bundle.putString("Overview", moviedetail.getmOverview());
            bundle.putString("voteAverage", moviedetail.getmVoteAverage());
            mDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_details,mDetailFragment).commit();

        }
    }

}
