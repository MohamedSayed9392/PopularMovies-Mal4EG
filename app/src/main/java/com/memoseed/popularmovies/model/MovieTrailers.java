package com.memoseed.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Moham on 7/30/2016.
 */
public class MovieTrailers {
    @SerializedName("key")
    String key;
    @SerializedName("site")
    String site;

    public MovieTrailers(String key, String site) {
        this.key = key;
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
