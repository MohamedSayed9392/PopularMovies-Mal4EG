package com.memoseed.popularmovies.activities;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.memoseed.popularmovies.AppParameters;
import com.memoseed.popularmovies.R;
import com.memoseed.popularmovies.utils.UTils;
import com.memoseed.popularmovies.adapter.MoviesRViewAdapter;
import com.memoseed.popularmovies.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    AppParameters p;
    ProgressDialog pD;

    RecyclerView rViewMovies;
    GridLayoutManager gridPortrait = new GridLayoutManager(this, 2);
    List<MovieItem> listMovies = new ArrayList<>();
    public static MoviesRViewAdapter moviesRViewAdapter;

    public static boolean favourite = false, pop_movies = true, top_rated = false;
    int listPosition = -1;

    public static boolean twoPane = false;
    public static MovieItem movieItem;
    public static FrameLayout item_detail_container;

    String urlPopular, urlTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
            item_detail_container = (FrameLayout) findViewById(R.id.movie_detail_container);
        }

        p = new AppParameters(this);
        pD = new ProgressDialog(this);

        urlPopular = getResources().getString(R.string.imdp_api_base_url) +
                getResources().getString(R.string.popular_movies) +
                getResources().getString(R.string.imdp_api_key);

        urlTopRated = getResources().getString(R.string.imdp_api_base_url) +
                getResources().getString(R.string.top_rated_movies) +
                getResources().getString(R.string.imdp_api_key);

        moviesRViewAdapter = new MoviesRViewAdapter(this, listMovies);
        rViewMovies = (RecyclerView) findViewById(R.id.rViewMovies);
        rViewMovies.setLayoutManager(gridPortrait);
        rViewMovies.setAdapter(moviesRViewAdapter);

        getMovies(urlPopular, true);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (listPosition != -1) {
            gridPortrait.scrollToPosition(listPosition);
        }
        if (UTils.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT) {
            twoPane = false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("listPosition", gridPortrait.findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        listPosition = savedInstanceState.getInt("listPosition");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular:
                getMovies(urlPopular, true);
                pop_movies = true;top_rated = false;favourite = false;

                return true;
            case R.id.action_top_rated:
                getMovies(urlTopRated, false);
                pop_movies = false;top_rated = true;favourite = false;

                return true;
            case R.id.action_fav:
                moviesRViewAdapter.resetMovies(UTils.getListMovies("fav_list",p));
                favourite = true;
                setTitle(getString(R.string.Fav_title));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (favourite && pop_movies) {
            getMovies(urlPopular, true);
        } else if (favourite && top_rated) {
            getMovies(urlTopRated, false);
        } else {
            super.onBackPressed();
        }
    }

    private void getMovies(String url, final boolean popular) {

        favourite = false;

        //get Cashed
        if (popular) {
            setTitle(getResources().getString(R.string.Popular_title));
            moviesRViewAdapter.resetMovies(UTils.getListMovies("popular_list",p));
        } else {
            setTitle(getResources().getString(R.string.Top_title));
            moviesRViewAdapter.resetMovies(UTils.getListMovies("top_rated_list",p));
        }

        //get Online
        if (UTils.isOnline(this)) {
            UTils.showProgressDialog("Loading ...", "Please wait", pD);
            StringRequest getTopRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    UTils.hideProgressDialog(pD);
                    List<MovieItem> tempList = new ArrayList<>();
                    try {
                        JSONArray results = (new JSONObject(response)).getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            tempList.add((new GsonBuilder().create().fromJson(results.getJSONObject(i).toString(), MovieItem.class)));
                        }
                        moviesRViewAdapter.resetMovies(tempList);
                        if (popular) {
                            UTils.saveListMovies(listMovies, "popular_list",p);
                        } else {
                            UTils.saveListMovies(listMovies, "top_rated_list",p);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    UTils.hideProgressDialog(pD);
                    Toast.makeText(getApplicationContext(), R.string.ServerError, Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(this).add(getTopRequest);
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoInternet, Toast.LENGTH_LONG).show();
        }
    }


   
}
