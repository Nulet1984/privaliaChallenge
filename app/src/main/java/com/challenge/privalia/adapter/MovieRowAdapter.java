package com.challenge.privalia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.challenge.privalia.R;
import com.challenge.privalia.configuration.MovieConfig;
import com.challenge.privalia.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raulpascual on 30/11/16.
 */
public class MovieRowAdapter extends BaseAdapter implements Filterable{

    Context context;
    private static LayoutInflater inflater = null;
    private List<Movie> movieList;
    private List<Movie> movieFilteredList;
    private Filter movieTitleFilter;

    public MovieRowAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.movieFilteredList = movieList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movieFilteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieFilteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.movie_row, null);
        }
        ImageView moviePictureImageView = (ImageView) view.findViewById(R.id.moviePictureImageView);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView yearTextView = (TextView) view.findViewById(R.id.yearTextView);
        TextView overviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
        titleTextView.setText(movieFilteredList.get(position).getTitle());
        yearTextView.setText(movieFilteredList.get(position).getRelease_date().substring(0, 4));
        overviewTextView.setText(movieFilteredList.get(position).getOverview());
        Picasso.with(context).load(MovieConfig.IMAGE_PRE_URL + movieFilteredList.get(position).getPoster_path()).into(moviePictureImageView);
        return view;
    }

    @Override
    public Filter getFilter() {
        if (movieTitleFilter == null) {
            movieTitleFilter = new MovieTitleFilter();
        }

        return movieTitleFilter;
    }

    private class MovieTitleFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<Movie> movieTempList = new ArrayList<Movie>();

                for (Movie movie : movieList) {
                    if (movie.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        movieTempList.add(movie);
                    }
                }

                filterResults.count = movieTempList.size();
                filterResults.values = movieTempList;

            } else {

                filterResults.count = movieList.size();
                filterResults.values = movieList;
            }

            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movieFilteredList = (ArrayList<Movie>) results.values;
            notifyDataSetChanged();
        }
    }
}



