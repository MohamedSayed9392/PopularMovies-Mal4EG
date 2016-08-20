package com.memoseed.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mohamed Sayed on 7/30/2016.
 */
public class MovieItem implements Serializable {
    @SerializedName("poster_path")
    String poster_path;
    @SerializedName("backdrop_path")
    String backdrop_path;
    @SerializedName("overview")
    String overview;
    @SerializedName("release_date")
    String release_date;
    @SerializedName("id")
    String id;
    @SerializedName("title")
    String title;
    @SerializedName("popularity")
    double popularity;
    @SerializedName("vote_average")
    double vote_average;

    public MovieItem(String id,String overview,double popularity,String poster_path,String backdrop_path,String release_date,String title,double vote_average ) {
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
}
