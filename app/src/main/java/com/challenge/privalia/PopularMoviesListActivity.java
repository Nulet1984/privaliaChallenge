package com.challenge.privalia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.service.MovieService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@EActivity(R.layout.activity_popular_movies_list)
public class PopularMoviesListActivity extends AppCompatActivity {

    @Bean
    MovieService movieService;

    @ViewById
    ListView movieListView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        movieService.getPopularMovies(1);
    }


    @Subscribe
    public void onMoviesReadyEvent(MoviesReadyEvent event) {
        movieListView.setAdapter(new MovieRowAdapter(this, movieService.getMovieList()));
    }


}
