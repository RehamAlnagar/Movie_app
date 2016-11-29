package com.example.android.movieapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION =1;
    //file name
    private static final String DATABASE_NAME = "Movies.db";
    //table name
    private static final String TABLE_MOVIES = "Movies";
    //table cols
    private static final String COL_ID = "id";
    private static final String COL_MOVIENAME = "title";
    private static final String COL_MOVIEPOSTER = "poster";
    private static final String COL_RELEASEDATE = "releasedate";
    private static final String COL_VOTEAVG = "voteaverage";
    private static final String COL_OVERVIEW = "overview";
    private Context mcontext;


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create table " + TABLE_MOVIES + " (" + COL_ID + " INTEGER PRIMARY KEY, " +
                COL_MOVIENAME +" TEXT, " + COL_MOVIEPOSTER + " TEXT, " + COL_RELEASEDATE + " TEXT, "+
                COL_VOTEAVG + " TEXT, " +COL_OVERVIEW + " TEXT" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    //add new raw to the database
    public boolean addMovies(Movie movies){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,movies.getmID());
        contentValues.put(COL_MOVIENAME,movies.getMoviename());
        contentValues.put(COL_MOVIEPOSTER,movies.getMovieposter());
        contentValues.put(COL_RELEASEDATE,movies.getmReleasedate());
        contentValues.put(COL_VOTEAVG,movies.getmVoteAverage());
        contentValues.put(COL_OVERVIEW,movies.getmOverview());

        Long result = db.insert(TABLE_MOVIES,null,contentValues);
        if(result == -1){
            Toast.makeText(mcontext, "Movie is already added to favourite", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Toast.makeText(mcontext, "Movie added to favourite", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_MOVIES,null);
        return result;
    }

    public Integer deleteMovies(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MOVIES, "id = ?" ,new String[]{id});

    }
}
