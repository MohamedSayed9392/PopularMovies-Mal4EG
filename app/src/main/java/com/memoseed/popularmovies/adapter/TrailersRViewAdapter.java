package com.memoseed.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.memoseed.popularmovies.R;
import com.memoseed.popularmovies.utils.UTils;
import com.memoseed.popularmovies.model.MovieTrailers;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Mohamed Sayed on 2/28/2016.
 */
public class TrailersRViewAdapter extends RecyclerView.Adapter<TrailersRViewAdapter.View_Holder> {

    private List<MovieTrailers> listTrailers;
    Context con;
    String TAG = this.getClass().getSimpleName();

    public TrailersRViewAdapter(Context con, List<MovieTrailers> listTrailers) {
        this.listTrailers = listTrailers;
        this.con = con;
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_trailler, parent, false);
        View_Holder holder = new View_Holder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        final MovieTrailers trailer = listTrailers.get(position);
        Picasso.with(con).load("http://img.youtube.com/vi/"+trailer.getKey()+"/0.jpg").into(holder.imTrailer);
        holder.rlTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UTils.openUrl((Activity)con,"http://www.youtube.com/watch?v="+trailer.getKey());
            }
        });

    }

    public void addTrailer(MovieTrailers trailer){
        listTrailers.add(trailer);
        notifyDataSetChanged();
    }

    public void addTrailers(List<MovieTrailers> trailers){
        listTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public void resetTrailers(){
        listTrailers.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listTrailers.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {

        RelativeLayout rlTrailer;
        ImageView imTrailer;

        View_Holder(View itemView) {
            super(itemView);
            rlTrailer = (RelativeLayout) itemView.findViewById(R.id.rlTrailer);
            imTrailer = (ImageView) itemView.findViewById(R.id.imTrailer);
        }
    }
}
