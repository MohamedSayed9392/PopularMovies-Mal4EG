package com.memoseed.popularmovies.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.Display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.memoseed.popularmovies.AppParameters;
import com.memoseed.popularmovies.database.DatabaseHandler;
import com.memoseed.popularmovies.database.FavouriteContentProvider;
import com.memoseed.popularmovies.model.MovieItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Sayed on 2/28/2016.
 */
public class UTils {

    public static boolean favourite = false, pop_movies = true, top_rated = false;

    public static boolean isOnline(Context context) {
        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    connected = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    connected = true;
        }
        return connected;
    }


    public static void openUrl(Activity activity, String Url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Url));
        activity.startActivity(i);
    }

    public static void shareText(Activity activity, String text, String title) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, title));
    }


    public static void showProgressDialog(String title, String message, ProgressDialog progressDialog) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog(ProgressDialog progressDialog) {
        progressDialog.hide();
    }

    public static int getScreenOrientation(Activity activity)
    {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
/*
    public static void saveListMovies(List<MovieItem> list, String saveName, AppParameters p) {
        Type listsType = new TypeToken<List<MovieItem>>() {}.getType();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(list, listsType);
        JsonArray jsonArray = element.getAsJsonArray();
        p.setString(jsonArray.toString(), saveName);
    }

    public static List<MovieItem> getListMovies(String saveName , AppParameters p) {
        Type listsType = new TypeToken<List<MovieItem>>() {}.getType();
        Gson gson = new Gson();
        String moviesList = p.getString(saveName);
        List<MovieItem> list = new ArrayList<>();
        if (gson.fromJson(moviesList, listsType) != null) {
            list = gson.fromJson(moviesList, listsType);
        }
        return list;
    }

    public static void editMovieFav(MovieItem movie, boolean add, AppParameters p) {
        List<MovieItem> list;
        list = getListMovies("fav_list",p);
        if (add) {
            list.add(movie);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (movie.getId().matches(list.get(i).getId())) {
                    list.remove(list.get(i));
                    break;
                }
            }
        }
        saveListMovies(list, "fav_list",p);
    }*/

    public static ContentValues getContentValuesOfMovie(MovieItem movieItem){

        ContentValues initialValues = new ContentValues();
        initialValues.put("id",movieItem.getId());
        initialValues.put("title", movieItem.getTitle());
        initialValues.put("poster_path", movieItem.getPoster_path());
        initialValues.put("backdrop_path", movieItem.getBackdrop_path());
        initialValues.put("overview", movieItem.getOverview());
        initialValues.put("release_date", movieItem.getRelease_date());
        initialValues.put("type", movieItem.getType());
        initialValues.put("popularity", movieItem.getPopularity());
        initialValues.put("vote_average", movieItem.getVote_average());

        return initialValues;
    }

    // Getting All MovieItems
    public static List<MovieItem> getAllMovieItems(Context context,String TABLE) {
        Uri contentUri = Uri.withAppendedPath(FavouriteContentProvider.CONTENT_URI, TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri,null, null, null,null);

        List<MovieItem> movieItemList = new ArrayList<MovieItem>();
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
}
