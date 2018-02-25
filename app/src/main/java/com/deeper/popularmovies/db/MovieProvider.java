package com.deeper.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final int CODE_REVIEWS = 200;
    public static final int CODE_REVIEWS_WITH_ID = 201;

    public static final int CODE_VIDEOS = 300;
    public static final int CODE_VIDEOS_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDatabaseHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, CODE_REVIEWS);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/#", CODE_REVIEWS_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_VIDEOS, CODE_VIDEOS);
        matcher.addURI(authority, MoviesContract.PATH_VIDEOS + "/#", CODE_VIDEOS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String tableName;
        switch (sUriMatcher.match(uri)) {

            case CODE_REVIEWS:
            case CODE_REVIEWS_WITH_ID:
                tableName = MoviesContract.ReviewEntry.TABLE_NAME;
                break;

            case CODE_VIDEOS:
            case CODE_VIDEOS_WITH_ID:
                tableName = MoviesContract.VideoEntry.TABLE_NAME;
                break;

            default:
                return super.bulkInsert(uri, values);
        }

        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1)
                    rowsInserted++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String tableName;
        String select = selection;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                tableName = MoviesContract.MovieEntry.TABLE_NAME;
                break;

            case CODE_MOVIE_WITH_ID:
                tableName = MoviesContract.MovieEntry.TABLE_NAME;
                select = MoviesContract.MovieEntry.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;

            case CODE_REVIEWS_WITH_ID:
                tableName = MoviesContract.ReviewEntry.TABLE_NAME;
                select = MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + "=" + uri.getLastPathSegment();
                break;

            case CODE_VIDEOS_WITH_ID:
                tableName = MoviesContract.VideoEntry.TABLE_NAME;
                select = MoviesContract.VideoEntry.COLUMN_MOVIE_ID + "=" + uri.getLastPathSegment();
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursorMovies = mOpenHelper.getReadableDatabase().query(
                tableName,
                projection,
                select,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursorMovies.setNotificationUri(getContext().getContentResolver(), uri);
        return cursorMovies;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();

                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        MoviesContract.MovieEntry.COLUMN_ID + "=" + id,
                        selectionArgs);
                numRowsDeleted += mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.ReviewEntry.TABLE_NAME,
                        MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + "=" + id,
                        selectionArgs);
                numRowsDeleted += mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.VideoEntry.TABLE_NAME,
                        MoviesContract.VideoEntry.COLUMN_MOVIE_ID + "=" + id,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return numRowsDeleted;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String tableName;
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_WITH_ID:
                tableName = MoviesContract.MovieEntry.TABLE_NAME;
                break;

            case CODE_REVIEWS_WITH_ID:
                tableName = MoviesContract.ReviewEntry.TABLE_NAME;
                break;

            case CODE_VIDEOS_WITH_ID:
                tableName = MoviesContract.VideoEntry.TABLE_NAME;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        db.beginTransaction();
        boolean inserted;
        try {
            inserted = db.insert(tableName, null, values) != -1;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (inserted && !tableName.equals(MoviesContract.MovieEntry.TABLE_NAME)) {
            getContext().getContentResolver().notifyChange(uri, null);

            return uri;
        }
        else
            return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Update is not implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("GetType is not implemented.");
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
