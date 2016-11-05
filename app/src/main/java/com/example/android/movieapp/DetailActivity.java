package com.example.android.movieapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_detail);
       /* if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.details, new DetailFragment())
                    .commit();
        }*/
    }

    public static class DetailFragment extends Fragment {
    }

}
