package com.deeper.popularmovies.utils;

import android.content.ContentValues;

import com.deeper.popularmovies.api.model.movieList.MovieListResult;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;
import com.deeper.popularmovies.api.model.videos.VideoResult;
import com.deeper.popularmovies.db.MoviesContract.MovieEntry;
import com.deeper.popularmovies.db.MoviesContract.ReviewEntry;
import com.deeper.popularmovies.db.MoviesContract.VideoEntry;

import java.util.List;

public class ContentValuesHelper {

    public static ContentValues toContentValues(MovieListResult movie) {
        ContentValues values = new ContentValues();

        values.put(MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        return values;
    }

    public static ContentValues toContentValues(ReviewResult review) {
        ContentValues values = new ContentValues();

        values.put(ReviewEntry.COLUMN_ID, review.getId());
        values.put(ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        values.put(ReviewEntry.COLUMN_CONTENT, review.getContent());
        values.put(ReviewEntry.COLUMN_MOVIE_ID, review.getMovieId());

        return values;
    }

    public static ContentValues[] toReviewArrayContentValues(List<ReviewResult> reviews) {
        ContentValues[] values = new ContentValues[reviews.size()];

        int i = 0;
        for (ReviewResult review : reviews) {
            values[i] = toContentValues(review);
            i++;
        }

        return values;
    }

    public static ContentValues toContentValues(VideoResult video) {
        ContentValues values = new ContentValues();

        values.put(VideoEntry.COLUMN_ID, video.getId());
        values.put(VideoEntry.COLUMN_KEY, video.getKey());
        values.put(VideoEntry.COLUMN_NAME, video.getName());
        values.put(VideoEntry.COLUMN_MOVIE_ID, video.getMovieId());

        return values;
    }

    public static ContentValues[] toVideoArrayContentValues(List<VideoResult> videos) {
        ContentValues[] values = new ContentValues[videos.size()];

        int i = 0;
        for (VideoResult video : videos) {
            values[i] = toContentValues(video);
            i++;
        }

        return values;
    }
}
