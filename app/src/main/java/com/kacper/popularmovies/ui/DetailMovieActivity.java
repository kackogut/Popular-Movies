package com.kacper.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kacper.popularmovies.services.AddingToDatabaseService;
import com.kacper.popularmovies.utilities.FetchMovieVolley;
import com.kacper.popularmovies.R;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.data.provider.MoviesDBContract;
import com.kacper.popularmovies.listeners.DetailedMovieListener;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

import static com.kacper.popularmovies.data.provider.MoviesDBContract.MovieEntry.TABLE_NAME;


public class DetailMovieActivity extends AppCompatActivity implements DetailedMovieListener {

    @BindView(R.id.detailed_movie_overwiev) TextView overwiew;
    @BindView(R.id.poster_image) ImageView poster;
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.air_date) TextView airDate;
    @BindView(R.id.trailer1) Button playTrailer1;
    @BindView(R.id.trailer2) Button playTrailer2;
    @BindView(R.id.reviews_text)
    TextView reviewsText;
    @BindView(R.id.reviews_view)
    LinearLayout linearLayout;
    @BindView(R.id.add_to_favourites_button)
    ImageView favouritedButton;



    private ArrayList<String> mTrailerURLs;
    private Movie mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_fragment);
        ButterKnife.bind(this);
        Intent intentThatStartedActivity = getIntent();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(intentThatStartedActivity!=null) {
            Bundle data = getIntent().getExtras();
            mMovie =  data.getParcelable("movie");
            Log.v("MovieID",mMovie.getMovieId());
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
                        setHeartImage(true);
                    }
            }
        }
    }


    @Override
    public void setTrailersURLs(ArrayList<String> trailers) {
        mTrailerURLs = trailers;
        if(mTrailerURLs.size()<1)
            playTrailer1.setVisibility(View.INVISIBLE);
        if(mTrailerURLs.size()<2)
            playTrailer2.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_movie_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.share_youtube_link){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if(mTrailerURLs.size()>0)
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTrailerURLs.get(0));
            else
                sendIntent.putExtra(Intent.EXTRA_TEXT,mMovie.getOverwiew());
            sendIntent.setType("text/plain");
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntent);
            }
            return true;
        }else
            return false;
    }
    @Override
    public void setReviews(HashMap<String, String> reviews) {

        if(reviews.isEmpty())
            reviewsText.setVisibility(View.INVISIBLE);
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
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmp));
        if(tmp!=null && sendIntent.resolveActivity(getPackageManager())!=null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tmp)));
    }
    public void addToFavourites(View view){
        if(!mMovie.isFavourited()) {
            Intent addingToDatabaseIntent = new Intent(this, AddingToDatabaseService.class);
            addingToDatabaseIntent.putExtra("movie",mMovie);
            startService(addingToDatabaseIntent);
            mMovie.setFavourited(true);
            setHeartImage(true);

        }else{
            Uri uri = MoviesDBContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(mMovie.getMovieId()).build();
            if(getContentResolver().delete(uri,null,null)>0){
                mMovie.setFavourited(false);
                setHeartImage(false);
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
    private void setHeartImage(boolean filled){
        if(filled)
            favouritedButton.setImageResource(R.drawable.hearts_filled_50);
           else
            favouritedButton.setImageResource(R.drawable.hearts_hollow_50);
    }


}
