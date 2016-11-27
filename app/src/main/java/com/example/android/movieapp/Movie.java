package com.example.android.movieapp;


public class Movie {

    private int mID;
    private String mMoviename;
    private String mMovieposter;
    private String mReleasedate;
    private String mVoteAverage;
    private String mOverview;


    public Movie() {

    }

    public Movie(String moviename, String movieposter) {
        this.mMoviename = moviename;
        this.mMovieposter = movieposter;
    }

    public String getMoviename() {
        return mMoviename;
    }

    public void setMoviename(String moviename) {
        this.mMoviename = moviename;
    }

    public String getMovieposter() {
        return mMovieposter;
    }

    public void setMovieposter(String movieposter) {
        this.mMovieposter = movieposter;
    }

    public String getmReleasedate() {
        return mReleasedate;
    }

    public void setmReleasedate(String mReleasedate) {
        this.mReleasedate = mReleasedate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public int getmID() { return mID; }

    public void setmID(int mID) { this.mID = mID; }

}
