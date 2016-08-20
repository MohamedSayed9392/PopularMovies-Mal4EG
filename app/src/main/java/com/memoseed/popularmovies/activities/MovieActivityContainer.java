package com.memoseed.popularmovies.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.memoseed.popularmovies.R;
import com.memoseed.popularmovies.fragments.MovieDetailFragment;

public class MovieActivityContainer extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_activity_container);

        Fragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setHasOptionsMenu(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, movieDetailFragment);
        fragmentTransaction.commit();

    }
}
