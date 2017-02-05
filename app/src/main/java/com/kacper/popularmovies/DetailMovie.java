package com.kacper.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.ImageSizes;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;


public class DetailMovie extends AppCompatActivity{

    @BindView(R.id.detailed_movie_overwiev) TextView overwiew;
    @BindView(R.id.poster_image) ImageView poster;
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.air_date) TextView airDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity!=null) {
            Bundle data = getIntent().getExtras();
            Movie movie = (Movie) data.getParcelable("movie");
            if(intentThatStartedActivity.hasExtra("movie")){
                title.setText(movie.getTitle());
                overwiew.setText(movie.getOverwiew());
                rating.setText("Average rating\n"+movie.getAverageRating());
                if (movie.getAirDate().isEmpty() && movie.getAirDate().length() != 0)
                    airDate.setText(movie.getAirDate().substring(0, 4));
                else
                    airDate.setText("Unknown");
                URL imageURL = NetworkUtils.buildImageURL(movie.getImageURI(), ImageSizes.MEDIUM);
                Picasso.with(this).load(imageURL.toString()).into(poster);
            }
        }
    }


}
