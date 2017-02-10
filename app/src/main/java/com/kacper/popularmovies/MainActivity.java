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
import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.SortingOrder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler, ThreadToUIListener{

    @BindView(R.id.poster_recycle_view) RecyclerView postersMovieRecyclerView;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.loading_indicator) ProgressBar progressBar;
    private SortingOrder actualSortingOrder=SortingOrder.POPULARITY_DESCENDING;
    private PosterAdapter posterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridLayoutManager = new GridLayoutManager(this,2);
        else
            gridLayoutManager = new GridLayoutManager(this,4);
        postersMovieRecyclerView.setLayoutManager(gridLayoutManager);
        postersMovieRecyclerView.setHasFixedSize(true);
        posterAdapter = new PosterAdapter(this,this);
        postersMovieRecyclerView.setAdapter(posterAdapter);
        loadData();
    }

    private void loadData(){
        if(isOnline()) {
            errorText.setText(R.string.error_message);
            posterAdapter.setAllMoviesOnPage(null);
            showMovieData();
            new FetchMovieVolley(this,this).getRequest(actualSortingOrder);

        }
        else{
            errorText.setText(R.string.internet_error);
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
                actualSortingOrder=SortingOrder.POPULARITY_DESCENDING;
                loadData();
                item.setChecked(true);
                return true;
            case R.id.order_top_rated:
                actualSortingOrder=SortingOrder.RATING_DESCENDING;
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
        errorText.setVisibility(View.INVISIBLE);
        postersMovieRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void showErrorMessage(String message){
        if(message!=null)
            errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);
        postersMovieRecyclerView.setVisibility(View.INVISIBLE);
    }
    @Override
    public void showProgressBar(boolean show){
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.INVISIBLE);
            postersMovieRecyclerView.setVisibility(View.INVISIBLE);
        }
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setMoviesToAdapter(ArrayList movies) {
        posterAdapter.setAllMoviesOnPage(movies);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
