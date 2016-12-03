package com.challenge.privalia.view.presenter;

import android.content.Context;

import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.event.NoMoreResultsEvent;
import com.challenge.privalia.event.RequestMoviesKOEvent;
import com.challenge.privalia.service.MovieService;
import com.challenge.privalia.view.PopularMoviesView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by raulpascual on 1/12/16.
 */
@EBean
public class PopularMoviesPresenter {

    @Bean
    MovieService movieService;

    @RootContext
    Context context;

    private MovieRowAdapter movieRowAdapter;
    private int pageNumber;
    private PopularMoviesView popularMoviesView;
    private boolean callToServiceDone;
    private String filterText;
    private boolean noMoreResults;

    public void initPresenter(PopularMoviesView popularMoviesView) {
        this.popularMoviesView = popularMoviesView;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        pageNumber = 0;
        callToServiceDone = true;
        loadDataInList();
    }

    public void loadDataInList() {
        if (callToServiceDone && !noMoreResults) {
            callToServiceDone = false;
            popularMoviesView.setLoading(true);
            movieService.getPopularMovies(++pageNumber);
        }
    }

    @Subscribe
    public void onMoviesReadyEvent(MoviesReadyEvent event) {

        if (movieRowAdapter == null) {
            movieRowAdapter = new MovieRowAdapter(context, movieService.getMovieList());
            popularMoviesView.setAdapterToList(movieRowAdapter);
            popularMoviesView.setLoading(false);
        } else {
            movieRowAdapter.getFilter().filter(filterText);
        }
        callToServiceDone = true;
        noMoreResults = false;

    }

    public MovieRowAdapter getMovieRowAdapter() {
        return movieRowAdapter;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    @Subscribe
    public void onRequestMoviesKOEvent(RequestMoviesKOEvent event) {
        callToServiceDone = true;
        pageNumber--;
        popularMoviesView.setLoading(false);
    }

    @Subscribe
    public void onNoMoreResultsEvent(NoMoreResultsEvent event) {
        callToServiceDone = true;
        popularMoviesView.setLoading(false);
        noMoreResults = true;
    }

    public boolean isCallToServiceDone() {
        return callToServiceDone;
    }

    public void setCallToServiceDone(boolean callToServiceDone) {
        this.callToServiceDone = callToServiceDone;
    }
}
