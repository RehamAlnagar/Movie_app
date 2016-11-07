package com.example.android.movieapp;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

       if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.details, new DetailFragment())
                    .commit();
        }

    }

   public  class DetailFragment extends Fragment {
       // private  final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){

            View rootView = inflater.inflate(R.layout.activity_detail, container, false);
            TextView Title = (TextView) findViewById(R.id.detail_Title);

            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();

            String MovieName = bundle.getString("Name");
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
                Title.setText(MovieName);

            }


            return rootView;

        }

    }

}
