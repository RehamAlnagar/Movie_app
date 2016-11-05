package com.example.android.movieapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

   public class customAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Movie> mMoviesDetail;

    public customAdapter(Context c,ArrayList<Movie> mMoviesDetail) {
        mContext = c;
        this.mMoviesDetail = mMoviesDetail ;
    }

    @Override
    public int getCount() {
        return mMoviesDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviesDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void add(ArrayList<Movie> movie){
        mMoviesDetail.clear();
        for (Movie m:movie ) {
            mMoviesDetail.add(m);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.item_list_movie, null);
        }
       // TextView Moviename = (TextView) view.findViewById(R.id.movie_name);
        ImageView Movieposter = (ImageView) view.findViewById(R.id.movie_poster);
       // Moviename.setText(mMoviesDetail.get(position).getMoviename());

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+ mMoviesDetail.get(position).getMovieposter()).into(Movieposter);
        return view;
    }


}
