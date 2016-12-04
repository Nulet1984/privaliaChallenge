package com.challenge.privalia.service;

import android.content.Context;

import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.event.NoMoreResultsEvent;
import com.challenge.privalia.event.RequestMoviesKOEvent;
import com.challenge.privalia.view.PopularMoviesView;
import com.challenge.privalia.view.presenter.PopularMoviesPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by raulpascual on 4/12/16.
 */

public class PopularMoviesPresenterTest {

    private PopularMoviesPresenter popularMoviesPresenter;
    private PopularMoviesService popularMoviesService;
    private PopularMoviesView popularMoviesView;
    private boolean callToServiceDone, noMoreResults;
    private MovieRowAdapter movieRowAdapter;
    private Context context;
    private MovieRowAdapter.MovieTitleFilter movieTitleFilter;

    @Before
    public void initTest() {
        popularMoviesPresenter = new PopularMoviesPresenter();
        popularMoviesService = mock(PopularMoviesService.class);
        popularMoviesView = mock(PopularMoviesView.class);
        movieRowAdapter = mock(MovieRowAdapter.class);
        context = mock(Context.class);
        movieTitleFilter = mock(MovieRowAdapter.MovieTitleFilter.class);
        movieRowAdapter.movieTitleFilter = movieTitleFilter;
        popularMoviesPresenter.context = context;
        popularMoviesPresenter.movieRowAdapter = movieRowAdapter;
        popularMoviesPresenter.popularMoviesView = popularMoviesView;
        popularMoviesPresenter.popularMoviesService = popularMoviesService;
        popularMoviesPresenter.callToServiceDone = callToServiceDone;
        popularMoviesPresenter.noMoreResults = noMoreResults;
    }

    /**
     * load data when the call to service is not done yet and there are more results
     */
    @Test
    public void loadDataInListTest1() {
        callToServiceDone = false;
        noMoreResults = false;
        popularMoviesPresenter.loadDataInList();
        verify(popularMoviesView, times(0)).setLoading(true);
        verify(popularMoviesService, times(0)).getPopularMovies(anyInt());
    }

    /**
     * load data when the call to service is done and there are more results
     */
    @Test
    public void loadDataInListTest2() {
        popularMoviesPresenter.callToServiceDone = true;
        popularMoviesPresenter.noMoreResults = false;
        popularMoviesPresenter.loadDataInList();
        verify(popularMoviesView, times(1)).setLoading(true);
        verify(popularMoviesService, times(1)).getPopularMovies(anyInt());
    }

    /**
     * load data when call to service is not done yet and there are not more results
     */
    @Test
    public void loadDataInListTest3() {
        popularMoviesPresenter.callToServiceDone = false;
        popularMoviesPresenter.noMoreResults = true;
        popularMoviesPresenter.loadDataInList();
        verify(popularMoviesView, times(0)).setLoading(true);
        verify(popularMoviesService, times(0)).getPopularMovies(anyInt());
    }

    /**
     * load data when call to service is done and there are not more results
     */
    @Test
    public void loadDataInListTest4() {
        popularMoviesPresenter.callToServiceDone = true;
        popularMoviesPresenter.noMoreResults = true;
        popularMoviesPresenter.loadDataInList();
        verify(popularMoviesView, times(0)).setLoading(true);
        verify(popularMoviesService, times(0)).getPopularMovies(anyInt());
    }

    /**
     * movies ready event is received when movieRowAdatper is null
     */
    @Test
    public void onMoviesReadyEventTest1() {
        popularMoviesPresenter.movieRowAdapter = null;
        popularMoviesPresenter.onMoviesReadyEvent(mock(MoviesReadyEvent.class));
        verify(popularMoviesView, times(1)).setAdapterToList(popularMoviesPresenter.movieRowAdapter);
        Assert.assertTrue(popularMoviesPresenter.callToServiceDone);
        Assert.assertFalse(popularMoviesPresenter.noMoreResults);
    }

    /**
     * moviesReadyEvent is received when movieRowAdatper is not null
     */
    @Test
    public void onMoviesReadyEventTest2() {
        when(popularMoviesPresenter.movieRowAdapter.getFilter()).thenReturn(movieRowAdapter.movieTitleFilter);
        popularMoviesPresenter.onMoviesReadyEvent(mock(MoviesReadyEvent.class));
        verify(popularMoviesPresenter.movieRowAdapter.getFilter(), times(1)).filter(null);
        Assert.assertTrue(popularMoviesPresenter.callToServiceDone);
        Assert.assertFalse(popularMoviesPresenter.noMoreResults);
    }

    /**
     * requestMoviesKoEvent is received (service has failed)
     */
    @Test
    public void onRequestMoviesKOEventTest1() {
        popularMoviesPresenter.onRequestMoviesKOEvent(mock(RequestMoviesKOEvent.class));
        Assert.assertTrue(popularMoviesPresenter.callToServiceDone);
        verify(popularMoviesView, times(1)).setLoading(false);
    }

    /**
     * noMoreResultsEvent is received (there are not more movies to load)
     */
    @Test
    public void onNoMoreResultsEventTest1() {
        popularMoviesPresenter.onNoMoreResultsEvent(mock(NoMoreResultsEvent.class));
        Assert.assertTrue(popularMoviesPresenter.callToServiceDone);
        verify(popularMoviesView, times(1)).setLoading(false);
        Assert.assertTrue(popularMoviesPresenter.noMoreResults);
    }

}
