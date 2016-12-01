package com.challenge.privalia.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class PopularMoviesListActivity extends AppCompatActivity implements MovieView{

    @ViewById
    ListView movieListView;

    @Bean
    MoviePresenter moviePresenter;


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
    public void setAdapterToList(MovieRowAdapter movieRowAdapter) {
       movieListView.setAdapter(movieRowAdapter);
   }


}
