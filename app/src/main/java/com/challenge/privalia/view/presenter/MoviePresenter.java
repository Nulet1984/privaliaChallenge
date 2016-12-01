package com.challenge.privalia.view.presenter;

import android.content.Context;

import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.service.MovieService;
import com.challenge.privalia.view.MovieView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by raulpascual on 1/12/16.
 */
@EBean
public class MoviePresenter {

    @Bean
    MovieService movieService;

    @RootContext
    Context context;

    private MovieRowAdapter movieRowAdapter;
    private int pageNumber;
    private MovieView movieView;
    private boolean callToServiceDone;
    private String filterText;

    public void initPresenter(MovieView movieView) {
        this.movieView = movieView;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        pageNumber = 0;
        loadDataInList();
    }

    public void loadDataInList() {
        if (!callToServiceDone) {
            callToServiceDone = true;
            movieService.getPopularMovies(++pageNumber);
        }
    }

    @Subscribe
    public void onMoviesReadyEvent(MoviesReadyEvent event) {
        if (movieRowAdapter == null) {
            movieRowAdapter = new MovieRowAdapter(context, movieService.getMovieList());
            movieView.setAdapterToList(movieRowAdapter);
        } else {
           movieRowAdapter.getFilter().filter(filterText);
        }
        callToServiceDone = false;
    }

    public MovieRowAdapter getMovieRowAdapter() {
        return movieRowAdapter;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

}
