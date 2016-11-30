package com.challenge.privalia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.challenge.privalia.R;
import com.challenge.privalia.configuration.MovieConfig;
import com.challenge.privalia.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by raulpascual on 30/11/16.
 */
public class MovieRowAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater = null;
    private List<Movie> movieList;

    public MovieRowAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
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
        titleTextView.setText(movieList.get(position).getTitle());
        yearTextView.setText(movieList.get(position).getRelease_date().substring(0, 4));
        overviewTextView.setText(movieList.get(position).getOverview());
        Picasso.with(context).load(MovieConfig.IMAGE_PRE_URL + movieList.get(position).getPoster_path()).into(moviePictureImageView);
        return view;
    }
}



