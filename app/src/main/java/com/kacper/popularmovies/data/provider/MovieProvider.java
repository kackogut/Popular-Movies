package com.kacper.popularmovies.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kacper on 12/02/2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_SINGLE_MOVIE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesDBContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesDBContract.PATH_MOVIES, CODE_MOVIES);
        matcher.addURI(authority, MoviesDBContract.PATH_MOVIES + "/#", CODE_SINGLE_MOVIE);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case CODE_SINGLE_MOVIE: {

                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesDBContract.MovieEntry.COLUMN_TITLE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_MOVIES: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri tmpReturnUri;
        int match = sUriMatcher.match(uri);
        switch (match){
            case CODE_MOVIES:
                long id = db.insert(MoviesDBContract.MovieEntry.TABLE_NAME,null,values);
                if(id>0){
                    tmpReturnUri = ContentUris.withAppendedId(MoviesDBContract.MovieEntry.CONTENT_URI,id);
                } else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }break;
            default:
                throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        return tmpReturnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        switch (sUriMatcher.match(uri)){
            case CODE_SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesDBContract.MovieEntry.TABLE_NAME,
                        "id=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: "+uri);
        }
        if(numRowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    public boolean CheckIfMovieExistInDatabase(String movieID) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String Query = "Select * from " + MoviesDBContract.MovieEntry.TABLE_NAME + " where " + MoviesDBContract.MovieEntry.MOVIE_SERVER_ID + " = " + movieID;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
