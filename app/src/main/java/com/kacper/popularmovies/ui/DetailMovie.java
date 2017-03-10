package com.kacper.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kacper.popularmovies.utilities.FetchMovieVolley;
import com.kacper.popularmovies.R;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.data.provider.MoviesDBContract;
import com.kacper.popularmovies.listeners.DetailedMovieListener;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

import static com.kacper.popularmovies.data.provider.MoviesDBContract.MovieEntry.TABLE_NAME;


public class DetailMovie extends AppCompatActivity implements DetailedMovieListener {

    @BindView(R.id.detailed_movie_overwiev) TextView overwiew;
    @BindView(R.id.poster_image) ImageView poster;
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.air_date) TextView airDate;
    @BindView(R.id.base_view) LinearLayout linearLayout;
    @BindView(R.id.add_to_favourites_button)
    Button favouritedButton;


    private ArrayList<String> mTrailerURLs;
    private Movie mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity!=null) {
            Bundle data = getIntent().getExtras();
            mMovie =  data.getParcelable("movie");
            if(intentThatStartedActivity.hasExtra("movie")){
                title.setText(mMovie.getTitle());
                overwiew.setText(mMovie.getOverwiew());
                rating.setText("Average rating\n"+mMovie.getAverageRating()+"/10");
                airDate.setText(mMovie.getAirDate().substring(0, 4));

                URL imageURL = NetworkUtils.buildImageURL(mMovie.getImageURI(), NetworkUtils.MEDIUM_SIZE_POSTER);
                Picasso.with(this).load(imageURL.toString()).into(poster);
                FetchMovieVolley fetchMovieVolley = new FetchMovieVolley(this,this);
                fetchMovieVolley.trailersRequest(mMovie.getMovieId());
                fetchMovieVolley.reviewRequest(mMovie.getMovieId());

                    if(checkIfMovieExistInDatabase(mMovie.getMovieId())){
                        mMovie.setFavourited(true);
                        favouritedButton.setText("Delete from favourites");
                    }
            }
        }
    }


    @Override
    public void setTrailersURLs(ArrayList<String> trailers) {
        mTrailerURLs = trailers;
    }

    @Override
    public void setReviews(HashMap<String, String> reviews) {

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
            tmp = mTrailerURLs.get(0);
        }else
            tmp = mTrailerURLs.get(1);
        if(tmp!=null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tmp)));
    }
    public void addToFavourites(View view){
        if(!mMovie.isFavourited()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesDBContract.MovieEntry.MOVIE_SERVER_ID, mMovie.getMovieId());
            contentValues.put(MoviesDBContract.MovieEntry.COLUMN_OVERWIEW, mMovie.getOverwiew());
            contentValues.put(MoviesDBContract.MovieEntry.COLUMN_POSTER, mMovie.getImageURI());
            contentValues.put(MoviesDBContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            contentValues.put(MoviesDBContract.MovieEntry.COLUMN_RATING, mMovie.getAverageRating());
            contentValues.put(MoviesDBContract.MovieEntry.COLUMN_AIR_DATE, mMovie.getAirDate());

            Uri uri = getContentResolver().insert(MoviesDBContract.MovieEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                mMovie.setFavourited(true);
                favouritedButton.setText("Delete from favourites");
            }
        }else{
            Uri uri = MoviesDBContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(mMovie.getMovieId()).build();
            if(getContentResolver().delete(uri,null,null)>0){
                mMovie.setFavourited(false);
                favouritedButton.setText("Add to favourites");
            }
        }

    }

    public boolean checkIfMovieExistInDatabase(String movieID) {
        String[] columns = { MoviesDBContract.MovieEntry.MOVIE_SERVER_ID};
        String selection = columns[0] + " =?";
        String[] selectionArgs = { movieID };

        Cursor cursor = getContentResolver().query(MoviesDBContract.MovieEntry.CONTENT_URI, columns, selection, selectionArgs, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}
