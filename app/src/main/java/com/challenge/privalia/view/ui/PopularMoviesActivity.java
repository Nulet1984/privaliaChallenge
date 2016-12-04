package com.challenge.privalia.view.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.challenge.privalia.R;
import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.view.PopularMoviesView;
import com.challenge.privalia.view.presenter.PopularMoviesPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Main activity to show results in a list
 */

@EActivity(R.layout.activity_popular_movies)
public class PopularMoviesActivity extends AppCompatActivity implements PopularMoviesView, SearchView.OnQueryTextListener{

    @ViewById
    ListView movieListView;

    @ViewById
    RelativeLayout loadingLayout;

    @Bean
    PopularMoviesPresenter popularMoviesPresenter;

    private MenuItem searchMenuItem;
    private SearchView searchView;
    private boolean stopSearching;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        popularMoviesPresenter.initPresenter(this);
        movieListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                hideKeyboard();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!stopSearching) {
                    if (totalItemCount - firstVisibleItem < 5) {
                        popularMoviesPresenter.loadDataInList();
                    } else {
                        setLoading(!popularMoviesPresenter.isCallToServiceDone());
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        EditText editText = (EditText) searchView.findViewById(R.id.search_src_text);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopSearching = false;
                return false;
            }
        });
        return true;
    }


   @Override
    public void setAdapterToList(MovieRowAdapter movieRowAdapter) {
       movieListView.setAdapter(movieRowAdapter);
   }

    /**
     * method to set loading on or off
     * @param loading
     */
    @Override
    public void setLoading(boolean loading) {
        if (loading) {
            loadingLayout.setVisibility(View.VISIBLE);
            movieListView.setEnabled(false);
        } else {
            loadingLayout.setVisibility(View.GONE);
            movieListView.setEnabled(true);
        }
    }

    /**
     * method to indicate app to stop searching results and hide loading
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        setLoading(false);
        hideKeyboard();
        stopSearching = true;
        return true;
    }

    /**
     * method to filter results at the list
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        stopSearching = false;
        popularMoviesPresenter.setFilterText(newText);
        popularMoviesPresenter.getMovieRowAdapter().getFilter().filter(newText);
        return true;
    }

    /**
     * method to hide keyboard when it is not necessary
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
