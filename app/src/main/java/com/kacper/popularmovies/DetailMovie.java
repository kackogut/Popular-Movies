package com.kacper.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;


public class DetailMovie extends AppCompatActivity implements DetailedMovieListener{

    @BindView(R.id.detailed_movie_overwiev) TextView overwiew;
    @BindView(R.id.poster_image) ImageView poster;
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.air_date) TextView airDate;
    @BindView(R.id.base_view) LinearLayout linearLayout;

    ArrayList<String> trailerURLs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity!=null) {
            Bundle data = getIntent().getExtras();
            Movie movie =  data.getParcelable("movie");
            if(intentThatStartedActivity.hasExtra("movie")){
                title.setText(movie.getTitle());
                overwiew.setText(movie.getOverwiew());
                rating.setText("Average rating\n"+movie.getAverageRating());
                airDate.setText(movie.getAirDate().substring(0, 4));

                URL imageURL = NetworkUtils.buildImageURL(movie.getImageURI(), NetworkUtils.MEDIUM_SIZE_POSTER);
                Picasso.with(this).load(imageURL.toString()).into(poster);
                FetchMovieVolley fetchMovieVolley = new FetchMovieVolley(this,this);
                fetchMovieVolley.trailersRequest(movie.getMovieId());
                fetchMovieVolley.reviewRequest(movie.getMovieId());
            }
        }
    }


    @Override
    public void setTrailersURLs(ArrayList<String> trailers) {
        trailerURLs = trailers;
    }

    @Override
    public void setReviews(HashMap<String, String> reviews) {
        Log.v("ss","ß");
        for(String key:reviews.keySet()){
            TextView textView = new TextView(this);
            TextView textView2 = new TextView(this);
            textView2.setText(key);
            textView2.setTypeface(null, Typeface.BOLD);
            textView.setTypeface(null, Typeface.ITALIC);
            textView.setTextSize(24);
            textView2.setTextSize(18);
            textView.setPadding(30,30,30,30);
            textView2.setPadding(30,30,30,30);
            textView2.setGravity(Gravity.RIGHT);
            textView.setText(reviews.get(key));
            linearLayout.addView(textView);
            linearLayout.addView(textView2);
        }
    }

    public void playTrailer(View v){
        String tmp = null;
        if(v.getId()==R.id.trailer1){
            tmp = trailerURLs.get(0);
        }else
            tmp = trailerURLs.get(1);
        if(tmp!=null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tmp)));
    }
}
