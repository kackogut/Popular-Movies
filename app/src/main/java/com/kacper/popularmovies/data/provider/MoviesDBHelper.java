package com.kacper.popularmovies.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kacper on 11/02/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_DB =
                    "CREATE TABLE "+MoviesDBContract.MovieEntry.TABLE_NAME +" (" +
                            MoviesDBContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            MoviesDBContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL "+
                            MoviesDBContract.MovieEntry.COLUMN_OVERWIEW + " TEXT NOT NULL "+
                            MoviesDBContract.MovieEntry.COLUMN_POSTER + " TEXT "+
                            MoviesDBContract.MovieEntry.COLUMN_FAVOURITED + " INTEGER NOT NULL "+ ");";

        sqLiteDatabase.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDBContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
