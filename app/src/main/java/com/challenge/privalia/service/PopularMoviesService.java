package com.challenge.privalia.service;


import com.challenge.privalia.event.MoviesReadyEvent;
import com.challenge.privalia.communication.RestClient;
import com.challenge.privalia.configuration.MovieConfig;
import com.challenge.privalia.event.NoMoreResultsEvent;
import com.challenge.privalia.event.RequestMoviesKOEvent;
import com.challenge.privalia.model.Movie;
import com.challenge.privalia.model.MovieList;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by raulpascual on 30/11/16.
 */
@EBean
public class PopularMoviesService {

    @Bean
    RestClient restClient;

    private List<Movie> movieList;

    public void getPopularMovies(int page) {
        Call<MovieList> callback = restClient.getService().getPopularMovies(MovieConfig.API_KEY, page);
        callback.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (movieList == null) {
                    movieList = response.body().getResults();
                    EventBus.getDefault().post(new MoviesReadyEvent());
                } else {
                    if (response.body() != null && response.body().getResults() != null) {
                        if (response.body().getResults().size() > 0) {
                            movieList.addAll(response.body().getResults());
                            EventBus.getDefault().post(new MoviesReadyEvent());
                        } else {
                            EventBus.getDefault().post(new NoMoreResultsEvent());
                        }
                    } else {
                        EventBus.getDefault().post(new RequestMoviesKOEvent());
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                EventBus.getDefault().post(new RequestMoviesKOEvent());
            }
        });
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
