package com.example.android.movieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();

        DetailFragment mDetailFragment = new DetailFragment();
        mDetailFragment.setArguments(sentBundle);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_details, mDetailFragment).commit();
    }


}
