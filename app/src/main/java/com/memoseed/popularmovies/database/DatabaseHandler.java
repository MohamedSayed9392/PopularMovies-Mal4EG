package com.memoseed.popularmovies.database;
 
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.memoseed.popularmovies.model.MovieItem;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PopularMoviesDB";
    public static final String TABLE_POPULAR_MOVIES = "popular_movies";
    public static final String TABLE_TOP_MOVIES = "top_movies";
    public static final String TABLE_FAV_MOVIES = "fav_movies";


    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_TYPE = "type";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + TABLE_POPULAR_MOVIES + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_POPULARITY + " DOUBLE,"
                + KEY_VOTE_AVERAGE + " DOUBLE"
                + ")";

        String CREATE_TOP_MOVIES_TABLE = "CREATE TABLE " + TABLE_TOP_MOVIES + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_POPULARITY + " DOUBLE,"
                + KEY_VOTE_AVERAGE + " DOUBLE"
                + ")";

        String CREATE_FAV_MOVIES_TABLE = "CREATE TABLE " + TABLE_FAV_MOVIES + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_POPULARITY + " DOUBLE,"
                + KEY_VOTE_AVERAGE + " DOUBLE"
                + ")";

        db.execSQL(CREATE_POPULAR_MOVIES_TABLE);
        db.execSQL(CREATE_TOP_MOVIES_TABLE);
        db.execSQL(CREATE_FAV_MOVIES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POPULAR_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOP_MOVIES);
        // Create tables again
        onCreate(db);
    }

    public void dropTable(String TABLE){
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE);
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_POPULARITY + " DOUBLE,"
                + KEY_VOTE_AVERAGE + " DOUBLE"
                + ")";
        this.getWritableDatabase().execSQL(CREATE_TABLE);
    }

    // Adding new movieItem
    public void addMovieItem(MovieItem movieItem,String TABLE) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, movieItem.getId());
        values.put(KEY_TITLE, movieItem.getTitle());
        values.put(KEY_POSTER_PATH, movieItem.getPoster_path());
        values.put(KEY_BACKDROP_PATH, movieItem.getBackdrop_path());
        values.put(KEY_OVERVIEW, movieItem.getOverview());
        values.put(KEY_RELEASE_DATE, movieItem.getRelease_date());
        values.put(KEY_TYPE, movieItem.getType());
        values.put(KEY_POPULARITY, movieItem.getPopularity());
        values.put(KEY_VOTE_AVERAGE, movieItem.getVote_average());
        // Inserting Row
        db.insert(TABLE, null, values);
        db.close(); // Closing database connection
    }
     
    // Getting All MovieItems
    public List<MovieItem> getAllMovieItems(String TABLE) {
        List<MovieItem> movieItemList = new ArrayList<MovieItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieItem movieItem = new MovieItem(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8));
                // Adding movieItem to list
                movieItemList.add(movieItem);
            } while (cursor.moveToNext());
        }
 
        // return movieItem list
        return movieItemList;
    }

    // Getting movieItems Count
    public int getMovieItemsCount(String TABLE) {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    // Getting movieItems Count
    public boolean isMovieFavouriteExist(String id) {
        String countQuery = "SELECT  * FROM " + TABLE_FAV_MOVIES +" where id = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        boolean found = false;
        if(cursor.getCount()>0){
            found = true;
        }else{
            found = false;
        }
        cursor.close();

        return found;

    }
 
}