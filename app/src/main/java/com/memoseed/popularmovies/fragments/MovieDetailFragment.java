package com.memoseed.popularmovies.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.gson.GsonBuilder;
import com.memoseed.popularmovies.AppParameters;
import com.memoseed.popularmovies.R;
import com.memoseed.popularmovies.activities.MainActivity;
import com.memoseed.popularmovies.activities.MovieActivityContainer;
import com.memoseed.popularmovies.database.DatabaseHandler;
import com.memoseed.popularmovies.database.FavouriteContentProvider;
import com.memoseed.popularmovies.utils.UTils;
import com.memoseed.popularmovies.adapter.TrailersRViewAdapter;
import com.memoseed.popularmovies.model.MovieItem;
import com.memoseed.popularmovies.model.MovieTrailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    String TAG = this.getClass().getSimpleName();

    AppParameters p;
    MovieItem movieItem;
    List<MovieTrailers> listTrailers = new ArrayList<>();

    FloatingActionButton fab;
    ImageView imCover;
    TextView txtYear, txtRate, txtOverview;
    RelativeLayout rlProgressT, rlNoDataT, rlProgressR, rlNoDataR, rlTryAgainT, rlTryAgainR;
    RecyclerView rViewTrailers;
    LinearLayout linearReviews, linDetails;
    CollapsingToolbarLayout toolbar_layout;

    TrailersRViewAdapter trailersRViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CoordinatorLayout llLayout = (CoordinatorLayout) inflater.inflate(R.layout.activity_movie, container, false);

        p = new AppParameters(mContext);

        fab = (FloatingActionButton) llLayout.findViewById(R.id.fab);


        Toolbar toolbar = (Toolbar) llLayout.findViewById(R.id.toolbar);

        toolbar_layout = (CollapsingToolbarLayout)llLayout.findViewById(R.id.toolbar_layout);
        linDetails = (LinearLayout) llLayout.findViewById(R.id.linDetails);
        rlProgressT = (RelativeLayout) llLayout.findViewById(R.id.rlProgressT);
        rlNoDataT = (RelativeLayout) llLayout.findViewById(R.id.rlNoDataT);
        rlProgressR = (RelativeLayout) llLayout.findViewById(R.id.rlProgressR);
        rlNoDataR = (RelativeLayout) llLayout.findViewById(R.id.rlNoDataR);
        rlTryAgainR = (RelativeLayout) llLayout.findViewById(R.id.rlTryAgainR);
        rlTryAgainT = (RelativeLayout) llLayout.findViewById(R.id.rlTryAgainT);
        txtYear = (TextView) llLayout.findViewById(R.id.txtYear);
        txtRate = (TextView) llLayout.findViewById(R.id.txtRate);
        txtOverview = (TextView) llLayout.findViewById(R.id.txtOverview);
        imCover = (ImageView) llLayout.findViewById(R.id.imCover);
        rViewTrailers = (RecyclerView) llLayout.findViewById(R.id.rViewTrailers);
        linearReviews = (LinearLayout) llLayout.findViewById(R.id.linearReviews);

        if (MainActivity.twoPane) {
            movieItem = MainActivity.movieItem;
            TextView txtMovieTitle = (TextView) llLayout.findViewById(R.id.txtMovieTitle);
            txtMovieTitle.setText(movieItem.getTitle());
        } else {
            ((MovieActivityContainer) mContext).setSupportActionBar(toolbar);
            ((MovieActivityContainer) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            movieItem = (MovieItem) mContext.getIntent().getExtras().getSerializable("movie");
            mContext.setTitle(movieItem.getTitle());
        }

        if (p.getBoolean(movieItem.getId(), false)) {
            fab.setImageResource(R.drawable.fav_checked);
        } else {
            fab.setImageResource(R.drawable.fav_unchecked);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p.getBoolean(movieItem.getId(), false)) {
                    fab.setImageResource(R.drawable.fav_unchecked);
                    p.setBoolean(false, movieItem.getId());
                    if (UTils.favourite) {
                        MainActivity.moviesRViewAdapter.removeMovie(movieItem);
                    }

                    Uri contentUri = Uri.withAppendedPath(FavouriteContentProvider.CONTENT_URI, DatabaseHandler.TABLE_FAV_MOVIES);
                    int resultUri =  mContext.getContentResolver().delete(contentUri, "id = ?", new String[] { String.valueOf(movieItem.getId()) });
                    Log.d(TAG,"Fav : "+String.valueOf(resultUri));
                    Log.d(TAG,String .valueOf(new DatabaseHandler(mContext).getMovieItemsCount(DatabaseHandler.TABLE_FAV_MOVIES)));
                 //   UTils.editMovieFav(movieItem, false,p);
                } else {
                    fab.setImageResource(R.drawable.fav_checked);
                    p.setBoolean(true, movieItem.getId());

                    Uri contentUri = Uri.withAppendedPath(FavouriteContentProvider.CONTENT_URI, DatabaseHandler.TABLE_FAV_MOVIES);
                    Uri resultUri =  mContext.getContentResolver().insert(contentUri, UTils.getContentValuesOfMovie(movieItem));
                    Log.d(TAG,"Fav : "+resultUri.toString());
                    Log.d(TAG,String .valueOf(new DatabaseHandler(mContext).getMovieItemsCount(DatabaseHandler.TABLE_FAV_MOVIES)));
                 //   UTils.editMovieFav(movieItem, true,p);
                }

                MainActivity.moviesRViewAdapter.notifyDataSetChanged();
               /* if(MainActivity.twoPane){
                    MainActivity.moviesRViewAdapter.notifyDataSetChanged();
                }*/
            }
        });


        txtYear.setText(movieItem.getRelease_date().substring(0, 4));
        txtRate.setText(movieItem.getVote_average() + "/10 ★");
        txtOverview.setText(movieItem.getOverview());

        Glide.with(mContext).load(getResources().getString(R.string.imdp_api_images_url) +
                getResources().getString(R.string.imdp_api_images_size_5) +
                movieItem.getBackdrop_path())
                .listener(GlidePalette.with(getResources().getString(R.string.imdp_api_images_url) +
                        getResources().getString(R.string.imdp_api_images_size_5) +
                        movieItem.getBackdrop_path())
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(linDetails)
                        .intoCallBack(
                                new GlidePalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        toolbar_layout.setContentScrimColor(palette.getVibrantColor(getResources().getColor(R.color.colorPrimary)));
                                    }
                                })).into(imCover);


        trailersRViewAdapter = new TrailersRViewAdapter(mContext, listTrailers);
        rViewTrailers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rViewTrailers.setAdapter(trailersRViewAdapter);
        getTrailers(getResources().getString(R.string.imdp_api_base_url) +
                movieItem.getId() +
                getResources().getString(R.string.imdp_api_videos_url) +
                getResources().getString(R.string.imdp_api_key));

        getReviews(getResources().getString(R.string.imdp_api_base_url) +
                movieItem.getId() +
                getResources().getString(R.string.imdp_api_revies_url) +
                getResources().getString(R.string.imdp_api_key));


        rlTryAgainR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReviews(getResources().getString(R.string.imdp_api_base_url) +
                        movieItem.getId() +
                        getResources().getString(R.string.imdp_api_revies_url) +
                        getResources().getString(R.string.imdp_api_key));
            }
        });

        rlTryAgainT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrailers(getResources().getString(R.string.imdp_api_base_url) +
                        movieItem.getId() +
                        getResources().getString(R.string.imdp_api_videos_url) +
                        getResources().getString(R.string.imdp_api_key));
            }
        });


        llLayout.findViewById(R.id.container_movie);
        return llLayout;
    }

    private Activity mContext;

    // called for API equal or above 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    /*
    * Deprecated on API 23
    */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                UTils.shareText(mContext, "https://www.themoviedb.org/movie/" + movieItem.getId(), "Share Movie Data");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getTrailers(String url) {
        if (UTils.isOnline(mContext)) {
            StringRequest getTrailersRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    try {
                        JSONArray results = (new JSONObject(response)).getJSONArray("results");
                        trailersRViewAdapter.resetTrailers();
                        for (int i = 0; i < results.length(); i++) {
                            trailersRViewAdapter.addTrailer((new GsonBuilder().create().fromJson(results.getJSONObject(i).toString(), MovieTrailers.class)));
                        }
                        rlProgressT.setVisibility(View.GONE);
                        rlTryAgainT.setVisibility(View.GONE);
                        if (results.length() == 0) {
                            rlNoDataT.setVisibility(View.VISIBLE);
                        } else {
                            rViewTrailers.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, R.string.ServerError, Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(mContext).add(getTrailersRequest);
        } else {
            rlProgressT.setVisibility(View.GONE);
            rlTryAgainT.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, R.string.NoInternet, Toast.LENGTH_LONG).show();
        }
    }

    private void getReviews(String url) {
        if (UTils.isOnline(mContext)) {
            StringRequest getReviewsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        JSONArray results = responseJSON.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject jsonObject = results.getJSONObject(i);
                            rlProgressR.setVisibility(View.GONE);
                            rlTryAgainR.setVisibility(View.GONE);
                            linearReviews.addView(reviewView(jsonObject.getString("author"),
                                    jsonObject.getString("content")));
                        }

                        if (results.length() == 0) {
                            rlProgressR.setVisibility(View.GONE);
                            rlTryAgainR.setVisibility(View.GONE);
                            rlNoDataR.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, R.string.ServerError, Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(mContext).add(getReviewsRequest);
        } else {
            rlProgressR.setVisibility(View.GONE);
            rlTryAgainR.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, R.string.NoInternet, Toast.LENGTH_LONG).show();
        }
    }

    public View reviewView(String author, String review) {
        View rowView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_review, null);

        TextView txtAuthor = (TextView) rowView.findViewById(R.id.txtAuthor);
        TextView txtReview = (TextView) rowView.findViewById(R.id.txtReview);

        txtAuthor.setText(author);
        txtReview.setText(review);

        return rowView;
    }

}
