package com.challenge.privalia.view.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.challenge.privalia.R;
import com.challenge.privalia.adapter.MovieRowAdapter;
import com.challenge.privalia.view.MovieView;
import com.challenge.privalia.view.presenter.MoviePresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_popular_movies_list)
public class PopularMoviesListActivity extends AppCompatActivity implements MovieView, SearchView.OnQueryTextListener{

    @ViewById
    ListView movieListView;

    @Bean
    MoviePresenter moviePresenter;

    private MenuItem searchMenuItem;
    private SearchView searchView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        moviePresenter.initPresenter(this);
        movieListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0 && totalItemCount - firstVisibleItem < 5) {
                    moviePresenter.loadDataInList();
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
        return true;
    }


   @Override
    public void setAdapterToList(MovieRowAdapter movieRowAdapter) {
       movieListView.setAdapter(movieRowAdapter);
   }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        moviePresenter.setFilterText(newText);
        moviePresenter.getMovieRowAdapter().getFilter().filter(newText);
        return true;
    }
}
