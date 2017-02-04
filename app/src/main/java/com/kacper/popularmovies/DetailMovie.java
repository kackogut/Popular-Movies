package com.kacper.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.kacper.popularmovies.enums.ImageSizes;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import it.sephiroth.android.library.picasso.Picasso;


public class DetailMovie extends AppCompatActivity{

    private TextView overwiew;
    private ImageView poster;
    private TextView title;
    private TextView rating;
    private TextView airDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);



        Intent intentThatStartedActivity = getIntent();
        overwiew = (TextView)findViewById(R.id.detailed_movie_overwiev);
        rating = (TextView)findViewById(R.id.rating);
        poster = (ImageView)findViewById(R.id.poster_image);
        title = (TextView)findViewById(R.id.movie_title);
        airDate = (TextView)findViewById(R.id.air_date);
        if(intentThatStartedActivity!=null) {


            if(intentThatStartedActivity.hasExtra("rating"))
                rating.setText(("Average rating:\n"+intentThatStartedActivity.getStringExtra("rating")+" / 10"));
            if(intentThatStartedActivity.hasExtra("poster")){
                URL imageURL = NetworkUtils.buildImageURL(intentThatStartedActivity.getStringExtra("poster"), ImageSizes.MEDIUM);
                Picasso.with(this).load(imageURL.toString()).into(poster);
            }
            if(intentThatStartedActivity.hasExtra("name"))
                title.setText(intentThatStartedActivity.getStringExtra("name"));
            if(intentThatStartedActivity.hasExtra("details"))
                overwiew.setText(intentThatStartedActivity.getStringExtra("details"));
            if(intentThatStartedActivity.hasExtra("air")) {
                String tmpAir = intentThatStartedActivity.getStringExtra("air");
                if (!tmpAir.isEmpty() && tmpAir.length() != 0)
                    airDate.setText(intentThatStartedActivity.getStringExtra("air").substring(0, 4));
                else
                    airDate.setText("Unknown");
            }
        }
    }


}
