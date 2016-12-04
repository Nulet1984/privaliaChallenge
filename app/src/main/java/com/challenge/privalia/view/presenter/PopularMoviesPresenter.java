package com.challenge.privalia.view.presenter;

import android.content.Context;

import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.event.NoMoreResultsEvent;
import com.challenge.privalia.event.RequestMoviesKOEvent;
import com.challenge.privalia.service.PopularMoviesService;
import com.challenge.privalia.view.PopularMoviesView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by raulpascual on 1/12/16.
 * Class to present results to the Activity
 */
@EBean
public class PopularMoviesPresenter {

    //Methods set to public because of tests
    @Bean
    public PopularMoviesService popularMoviesService;

    @RootContext
    public Context context;

    public MovieRowAdapter movieRowAdapter;
    private int pageNumber;
    public PopularMoviesView popularMoviesView;
    public boolean callToServiceDone;
    private String filterText;
    public boolean noMoreResults;

    /**
     * method to initiate presenter and call the first time to service
     * @param popularMoviesView
     */
    public void initPresenter(PopularMoviesView popularMoviesView) {
        this.popularMoviesView = popularMoviesView;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        pageNumber = 0;
        callToServiceDone = true;
        loadDataInList();
    }

    /**
     * method to call service if needed
     */
    public void loadDataInList() {
        if (callToServiceDone && !noMoreResults) {
            callToServiceDone = false;
            popularMoviesView.setLoading(true);
            popularMoviesService.getPopularMovies(++pageNumber);
        }
    }

    /**
     * method to receive MoviesReadyEvent to create and assign to view adapter or to add more elements to list
     * @param event
     */
    @Subscribe
    public void onMoviesReadyEvent(MoviesReadyEvent event) {
        if (movieRowAdapter == null) {
            movieRowAdapter = new MovieRowAdapter(context, popularMoviesService.getMovieList());
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

    /**
     * method to set the behaviour when the service fails
     * @param event
     */
    @Subscribe
    public void onRequestMoviesKOEvent(RequestMoviesKOEvent event) {
        callToServiceDone = true;
        pageNumber--;
        popularMoviesView.setLoading(false);
    }

    /**
     * method to set the behaviour when there are no more movies to load in the list
     * @param event
     */
    @Subscribe
    public void onNoMoreResultsEvent(NoMoreResultsEvent event) {
        callToServiceDone = true;
        popularMoviesView.setLoading(false);
        noMoreResults = true;
    }

    public boolean isCallToServiceDone() {
        return callToServiceDone;
    }

}
