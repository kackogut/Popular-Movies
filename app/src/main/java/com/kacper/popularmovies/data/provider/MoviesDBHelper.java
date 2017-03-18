package com.kacper.popularmovies.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.DropBoxManager;

/**
 * Created by kacper on 11/02/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 2;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_DB =
                    "CREATE TABLE "+MoviesDBContract.MovieEntry.TABLE_NAME +" (" +
                            MoviesDBContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            MoviesDBContract.MovieEntry.MOVIE_SERVER_ID + " INTEGER NOT NULL, "+
                            MoviesDBContract.MovieEntry.COLUMN_OVERWIEW + " TEXT NOT NULL, "+
                            MoviesDBContract.MovieEntry.COLUMN_POSTER + " TEXT, "+
                            MoviesDBContract.MovieEntry.COLUMN_FAVOURITED + " INTEGER, "+
                            MoviesDBContract.MovieEntry.COLUMN_RATING + " REAL, "+
                            MoviesDBContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "+
                            MoviesDBContract.MovieEntry.COLUMN_AIR_DATE + " TEXT NOT NULL, "+
                            " UNIQUE (" + MoviesDBContract.MovieEntry.MOVIE_SERVER_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(CREATE_DB);
    }
    private static final String DATABASE_ALTER_TABLE_1 = "ALTER TABLE "+ MoviesDBContract.MovieEntry.TABLE_NAME
            + " ADD COLUMN "+ MoviesDBContract.MovieEntry.COLUMN_AIR_DATE + " string;";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion<2){
            sqLiteDatabase.execSQL(DATABASE_ALTER_TABLE_1);
        }
    }
}
