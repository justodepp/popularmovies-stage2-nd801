package com.deeper.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deeper.popularmovies.db.MoviesContract.MovieEntry;
import com.deeper.popularmovies.db.MoviesContract.VideoEntry;
import com.deeper.popularmovies.db.MoviesContract.ReviewEntry;

public class MoviesDatabaseHelper extends SQLiteOpenHelper {

    // The name of the database
    public static final String DATABASE_NAME = "movies.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry.COLUMN_ID               + " TEXT PRIMARY KEY, "    +
                        MovieEntry.COLUMN_ORIGINAL_TITLE   + " TEXT NOT NULL, "       +
                        MovieEntry.COLUMN_OVERVIEW         + " TEXT NOT NULL, "       +
                        MovieEntry.COLUMN_POSTER_PATH      + " TEXT NOT NULL, "       +
                        MovieEntry.COLUMN_RELEASE_DATE     + " TEXT NOT NULL, "       +
                        MovieEntry.COLUMN_BACKDROP_PATH    + " TEXT NOT NULL, "       +
                        MovieEntry.COLUMN_VOTE_AVERAGE     + " REAL NOT NULL"         +
                        ");";

        final String SQL_CREATE_VIDEOS_TABLE =
                "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                        VideoEntry.COLUMN_ID               + " TEXT PRIMARY KEY, "    +
                        VideoEntry.COLUMN_KEY              + " TEXT NOT NULL, "       +
                        VideoEntry.COLUMN_NAME             + " TEXT NOT NULL, "       +
                        VideoEntry.COLUMN_MOVIE_ID         + " INTEGER NOT NULL"      +
                        ");";

        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                        ReviewEntry.COLUMN_ID              + " TEXT PRIMARY KEY, "    +
                        ReviewEntry.COLUMN_AUTHOR          + " TEXT NOT NULL, "       +
                        ReviewEntry.COLUMN_CONTENT         + " TEXT NOT NULL, "       +
                        ReviewEntry.COLUMN_MOVIE_ID        + " INTEGER NOT NULL"      +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
