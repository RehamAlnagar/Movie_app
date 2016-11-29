package com.example.android.movieapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<String> mMoviesDetail;

    public TrailerAdapter(Context c,ArrayList<String> mMoviesDetail) {
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


    public void add(ArrayList<String> movies){
        mMoviesDetail.clear();
        for (String m:movies ) {
            mMoviesDetail.add(m);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int trailernum = position + 1;
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_trailers, null);
        }
         TextView trailerText = (TextView) view.findViewById(R.id.list_item_trailer);
         ImageView trailerSymbol = (ImageView) view.findViewById(R.id.trailer_img);
         trailerText.setText("Trailer " + trailernum);
         trailerSymbol.setImageResource(R.drawable.iconplay);

        return view;
    }


}
