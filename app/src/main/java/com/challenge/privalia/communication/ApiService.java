package com.challenge.privalia.communication;

import com.challenge.privalia.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by raulpascual on 30/11/16.
 */

public interface ApiService {

    @GET("movie/popular")
    @Headers("Content-Type: application/json")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int numPage);

}
