package com.challenge.privalia.view;

import com.challenge.privalia.adapter.MovieRowAdapter;

/**
 * Created by raulpascual on 1/12/16.
 */

public interface PopularMoviesView {

    void setAdapterToList(MovieRowAdapter movieRowAdapter);

    void setLoading(boolean loading);
}
