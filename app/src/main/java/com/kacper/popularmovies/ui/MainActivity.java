package com.kacper.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kacper.popularmovies.utilities.FetchMovieVolley;
import com.kacper.popularmovies.R;
import com.kacper.popularmovies.listeners.ThreadToUIListener;
import com.kacper.popularmovies.adapter.NetworkMoviesAdapter;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.data.provider.MoviesDBContract;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NetworkMoviesAdapter.PosterAdapterOnClickHandler, ThreadToUIListener, LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.poster_recycle_view) RecyclerView mPostersMovieRecyclerView;
    @BindView(R.id.error_text) TextView mErrorText;
    @BindView(R.id.loading_indicator) ProgressBar mMoviesLoadingProgress;
    private String mActualSortingOrder;
    private NetworkMoviesAdapter mNetworkMoviesAdapter;

    private static final int ID_FAVOURITE_MOVIES_LOADER = 44;

    Parcelable savedRecyclerState;
    private static final String SAVED_ORDER_TEXT = "savedOrder";
    private static final String SAVED_LAYOUT_POSITION_TEXT = "savedPosition";

    private static final String[] MAIN_MOVIE_PROJECTION = {
            MoviesDBContract.MovieEntry._ID,
            MoviesDBContract.MovieEntry.COLUMN_POSTER,
            MoviesDBContract.MovieEntry.COLUMN_RATING,
            MoviesDBContract.MovieEntry.MOVIE_SERVER_ID,
            MoviesDBContract.MovieEntry.COLUMN_TITLE,
            MoviesDBContract.MovieEntry.COLUMN_OVERWIEW,
            MoviesDBContract.MovieEntry.COLUMN_AIR_DATE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_COLUMN_POSTER = 1;
    public static final int INDEX_MOVIE_COLUMN_RATING = 2;
    public static final int INDEX_MOVIE_SERVER_ID = 3;
    public static final int INDEX_MOVIE_TITLE = 4;
    public static final int INDEX_MOVIE_OVERWIEW = 5;
    public static final int INDEX_AIR_DATE = 6;

    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mActualSortingOrder==null)
            mActualSortingOrder = NetworkUtils.SORTING_POPULARITY;
        ButterKnife.bind(this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mGridLayoutManager = new GridLayoutManager(this,2);
        else
            mGridLayoutManager = new GridLayoutManager(this,4);
        mPostersMovieRecyclerView.setLayoutManager(mGridLayoutManager);
        mPostersMovieRecyclerView.setHasFixedSize(true);
        mNetworkMoviesAdapter = new NetworkMoviesAdapter(this,this);
        mPostersMovieRecyclerView.setAdapter(mNetworkMoviesAdapter);
        if((savedInstanceState!=null)){
            if(savedInstanceState.getString(SAVED_ORDER_TEXT)!=null)
                mActualSortingOrder = savedInstanceState.getString(SAVED_ORDER_TEXT);
        }
        loadData();


    }

    private void loadData(){
        if(mActualSortingOrder==NetworkUtils.FAVOURITED_MOVIES)
            getSupportLoaderManager().initLoader(ID_FAVOURITE_MOVIES_LOADER, null, this);
        else {
            if (isOnline()) {
                mErrorText.setText(R.string.error_message);
                mNetworkMoviesAdapter.setAllMoviesOnPage(null);
                new FetchMovieVolley(this, this).getRequest(mActualSortingOrder);
            } else {
                mErrorText.setText(R.string.internet_error);
                showErrorMessage(null);
            }
        }

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
                setToolbarTitleBasedOnContent();
                loadData();
                item.setChecked(true);
                return true;
            case R.id.order_top_rated:
                mActualSortingOrder =NetworkUtils.SORTING_RATING;
                setToolbarTitleBasedOnContent();
                loadData();
                item.setChecked(true);
                return true;
            case R.id.favourite_movies:
                mActualSortingOrder = NetworkUtils.FAVOURITED_MOVIES;
                setToolbarTitleBasedOnContent();
                item.setChecked(true);
                getSupportLoaderManager().initLoader(ID_FAVOURITE_MOVIES_LOADER, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean setToolbarTitleBasedOnContent(){
        if(getSupportActionBar() == null)
            return false;
        else {
            switch (mActualSortingOrder) {
                case NetworkUtils.SORTING_RATING:
                    getSupportActionBar().setTitle(getString(R.string.rating_toolbar_text));
                    return true;
                case NetworkUtils.SORTING_POPULARITY:
                    getSupportActionBar().setTitle(getString(R.string.popular_toolbar_text));
                    return true;
                case NetworkUtils.FAVOURITED_MOVIES:
                    getSupportActionBar().setTitle(getString(R.string.favourinted_toolbar_text));
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(getSupportActionBar() == null)
            return false;
        else {
            setToolbarTitleBasedOnContent();
            switch (mActualSortingOrder) {
                case NetworkUtils.SORTING_RATING:
                    menu.findItem(R.id.order_top_rated).setChecked(true);
                    return true;
                case NetworkUtils.SORTING_POPULARITY:
                    menu.findItem(R.id.order_popular).setChecked(true);
                    return true;
                case NetworkUtils.FAVOURITED_MOVIES:
                    menu.findItem(R.id.favourite_movies).setChecked(true);
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Context context = this;
        Class destinationClass = DetailMovieActivity.class;
        Intent intentToStartDetailMovie = new Intent(context, destinationClass);

        intentToStartDetailMovie.putExtra("movie",clickedMovie);
        startActivity(intentToStartDetailMovie);
    }


    @Override
    public void showMovieData(){
        mErrorText.setVisibility(View.INVISIBLE);
        mPostersMovieRecyclerView.setVisibility(View.VISIBLE);
        if(savedRecyclerState!=null)
            mPostersMovieRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerState);
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
        mNetworkMoviesAdapter.setAllMoviesOnPage(movies);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_FAVOURITE_MOVIES_LOADER:
                Uri moviesQueryUri = MoviesDBContract.MovieEntry.CONTENT_URI;
                String sortingOrder = MoviesDBContract.MovieEntry.MOVIE_SERVER_ID+" ASC";
                return new CursorLoader(this, moviesQueryUri, MAIN_MOVIE_PROJECTION, null,null,sortingOrder);
            default:
                throw new RuntimeException("Loader not implemented: "+id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Movie> moviesFromDatabase = new ArrayList<>();
        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext())
            moviesFromDatabase.add(new Movie(data.getString(INDEX_MOVIE_TITLE),data.getString(INDEX_MOVIE_OVERWIEW),data.getString(INDEX_MOVIE_COLUMN_RATING),data.getString(INDEX_AIR_DATE),
                    data.getString(INDEX_MOVIE_COLUMN_POSTER),data.getString(INDEX_MOVIE_SERVER_ID)));
        if(!moviesFromDatabase.isEmpty())
            setMoviesToAdapter(moviesFromDatabase);
        showMovieData();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_ORDER_TEXT,mActualSortingOrder);
        outState.putParcelable(SAVED_LAYOUT_POSITION_TEXT,mGridLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null && savedInstanceState.getParcelable(SAVED_LAYOUT_POSITION_TEXT)!=null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_POSITION_TEXT);
            mPostersMovieRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            savedRecyclerState = savedRecyclerLayoutState;
        }
    }
}
