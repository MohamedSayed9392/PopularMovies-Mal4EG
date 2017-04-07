package com.memoseed.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.memoseed.popularmovies.AppParameters;
import com.memoseed.popularmovies.R;
import com.memoseed.popularmovies.activities.MainActivity;
import com.memoseed.popularmovies.database.DatabaseHandler;
import com.memoseed.popularmovies.database.FavouriteContentProvider;
import com.memoseed.popularmovies.fragments.MovieDetailFragment;
import com.memoseed.popularmovies.activities.MovieActivityContainer;
import com.memoseed.popularmovies.model.MovieItem;
import com.memoseed.popularmovies.utils.UTils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Mohamed Sayed on 2/28/2016.
 */
public class MoviesRViewAdapter extends RecyclerView.Adapter<MoviesRViewAdapter.View_Holder> {

    private List<MovieItem> listMovies;
    Context con;
    String TAG = this.getClass().getSimpleName();

    public MoviesRViewAdapter(Context con, List<MovieItem> listMovies) {
        this.listMovies = listMovies;
        this.con = con;
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_movie, parent, false);
        View_Holder holder = new View_Holder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        final MovieItem movie = listMovies.get(position);
        holder.txtYear.setText(movie.getRelease_date().substring(0, 4));
        holder.txtPop.setText(Integer.toString((int) movie.getPopularity()));
        Picasso.with(con).load(con.getResources().getString(R.string.imdp_api_images_url) +
                con.getResources().getString(R.string.imdp_api_images_size_3) +
                movie.getPoster_path()).into(holder.imMovie, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        holder.cardMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.twoPane){
                    MainActivity.movieItem = movie;
                    attachFragment();
                }else {
                    con.startActivity(new Intent(con, MovieActivityContainer.class).putExtra("movie", movie));
                }
            }
        });

        holder.checkFav.setOnCheckedChangeListener(null);
        if(((MainActivity)con).databaseHandler.isMovieFavouriteExist(movie.getId())){
            holder.checkFav.setChecked(true);
        }else{
            holder.checkFav.setChecked(false);
        }


        holder.checkFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Uri contentUri = Uri.withAppendedPath(FavouriteContentProvider.CONTENT_URI, DatabaseHandler.TABLE_FAV_MOVIES);
                    Uri resultUri =  con.getContentResolver().insert(contentUri, UTils.getContentValuesOfMovie(movie));
                    Log.d(TAG,"Fav : "+resultUri.toString());
                    Log.d(TAG,String .valueOf(new DatabaseHandler(con).getMovieItemsCount(DatabaseHandler.TABLE_FAV_MOVIES)));
                }else{
                    Uri contentUri = Uri.withAppendedPath(FavouriteContentProvider.CONTENT_URI, DatabaseHandler.TABLE_FAV_MOVIES);
                    int resultUri =  con.getContentResolver().delete(contentUri, "id = ?", new String[] { String.valueOf(movie.getId()) });
                    Log.d(TAG,"Fav : "+String.valueOf(resultUri));
                    Log.d(TAG,String .valueOf(new DatabaseHandler(con).getMovieItemsCount(DatabaseHandler.TABLE_FAV_MOVIES)));

                    if(UTils.favourite){
                        listMovies.remove(movie);
                        notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void addMovie(MovieItem movie){
        listMovies.add(movie);
        notifyDataSetChanged();
    }

    public void removeMovie(MovieItem movie){
        listMovies.remove(movie);
        notifyDataSetChanged();
    }

    public void addMovies(List<MovieItem> movies){
        listMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void resetMovies(List<MovieItem> movies){
        listMovies.clear();
        listMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cardMovie;
        TextView txtPop, txtYear;
        CheckBox checkFav;
        ImageView imMovie;
        ProgressBar progressBar;

        View_Holder(View itemView) {
            super(itemView);
            cardMovie = (CardView) itemView.findViewById(R.id.cardMovie);
            txtPop = (TextView) itemView.findViewById(R.id.txtPop);
            txtYear = (TextView) itemView.findViewById(R.id.txtYear);
            checkFav = (CheckBox) itemView.findViewById(R.id.checkFav);
            imMovie = (ImageView) itemView.findViewById(R.id.imMovie);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
        }
    }

    public void attachFragment(){
        Fragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setHasOptionsMenu(true);
        FragmentManager fragmentManager = ((MainActivity)con).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(MainActivity.item_detail_container.getId(), movieDetailFragment);
        fragmentTransaction.commit();
    }
}
