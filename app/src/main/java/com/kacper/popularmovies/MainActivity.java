package com.kacper.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kacper.popularmovies.adapter.PosterAdapter;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler, ThreadToUIListener{

    @BindView(R.id.poster_recycle_view) RecyclerView mPostersMovieRecyclerView;
    @BindView(R.id.error_text) TextView mErrorText;
    @BindView(R.id.loading_indicator) ProgressBar mMoviesLoadingProgress;
    private String mActualSortingOrder;
    private PosterAdapter mPosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActualSortingOrder = NetworkUtils.SORTING_POPULARITY;
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridLayoutManager = new GridLayoutManager(this,2);
        else
            gridLayoutManager = new GridLayoutManager(this,4);
        mPostersMovieRecyclerView.setLayoutManager(gridLayoutManager);
        mPostersMovieRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter(this,this);
        mPostersMovieRecyclerView.setAdapter(mPosterAdapter);
        loadData();
    }

    private void loadData(){
        if(isOnline()) {
            mErrorText.setText(R.string.error_message);
            mPosterAdapter.setAllMoviesOnPage(null);
            new FetchMovieVolley(this,this).getRequest(mActualSortingOrder);

        }
        else{
            mErrorText.setText(R.string.internet_error);
            showErrorMessage(null);
        }

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.order_popular:
                mActualSortingOrder =NetworkUtils.SORTING_POPULARITY;
                loadData();
                item.setChecked(true);
                return true;
            case R.id.order_top_rated:
                mActualSortingOrder =NetworkUtils.SORTING_RATING;
                loadData();
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Context context = this;
        Class destinationClass = DetailMovie.class;
        Intent intentToStartDetailMovie = new Intent(context, destinationClass);
        intentToStartDetailMovie.putExtra("movie",clickedMovie);
        startActivity(intentToStartDetailMovie);
    }


    @Override
    public void showMovieData(){
        mErrorText.setVisibility(View.INVISIBLE);
        mPostersMovieRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void showErrorMessage(String message){
        if(message!=null)
            mErrorText.setText(message);
        mErrorText.setVisibility(View.VISIBLE);
        mPostersMovieRecyclerView.setVisibility(View.INVISIBLE);
    }
    @Override
    public void showProgressBar(boolean show){
        if(show) {
            mMoviesLoadingProgress.setVisibility(View.VISIBLE);
            mErrorText.setVisibility(View.INVISIBLE);
            mPostersMovieRecyclerView.setVisibility(View.INVISIBLE);
        }
        else
            mMoviesLoadingProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setMoviesToAdapter(ArrayList movies) {
        mPosterAdapter.setAllMoviesOnPage(movies);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {
        //TODO: Clear connection and close lifecycle
        super.onDestroy();
    }
}
