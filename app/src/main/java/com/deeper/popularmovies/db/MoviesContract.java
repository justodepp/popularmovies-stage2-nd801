package com.deeper.popularmovies.db;

import android.net.Uri;

import com.deeper.popularmovies.api.model.movieList.MovieListResult;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;
import com.deeper.popularmovies.api.model.videos.VideoResult;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.deeper.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

    public static final String[] projectionMovie = {
            MovieEntry.COLUMN_ID,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_BACKDROP_PATH,
            MovieEntry.COLUMN_RELEASE_DATE
    };

    public static final String[] projectionReview = {
            ReviewEntry.COLUMN_ID,
            ReviewEntry.COLUMN_AUTHOR,
            ReviewEntry.COLUMN_CONTENT,
            ReviewEntry.COLUMN_MOVIE_ID
    };

    public static final String[] projectionTrailer = {
            VideoEntry.COLUMN_ID,
            VideoEntry.COLUMN_KEY,
            VideoEntry.COLUMN_NAME,
            VideoEntry.COLUMN_MOVIE_ID
    };

    public static final class MovieEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = MovieListResult.ID;
        public static final String COLUMN_VOTE_AVERAGE = MovieListResult.VOTE_AVERAGE;
        public static final String COLUMN_POSTER_PATH = MovieListResult.POSTER_PATH;
        public static final String COLUMN_ORIGINAL_TITLE = MovieListResult.ORIGINAL_TITLE;
        public static final String COLUMN_OVERVIEW = MovieListResult.OVERVIEW;
        public static final String COLUMN_BACKDROP_PATH = MovieListResult.BACKDROP_PATH;
        public static final String COLUMN_RELEASE_DATE = MovieListResult.RELEASE_DATE;
        
        public static Uri buildMovieUri(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    public static final class ReviewEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_ID = ReviewResult.ID;
        public static final String COLUMN_AUTHOR = ReviewResult.AUTHOR;
        public static final String COLUMN_CONTENT = ReviewResult.CONTENT;
        public static final String COLUMN_MOVIE_ID = ReviewResult.MOVIE_ID;

        public static Uri buildReviewsUri(int movie_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movie_id))
                    .build();
        }
    }

    public static final class VideoEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEOS)
                .build();

        public static final String TABLE_NAME = "videos";

        public static final String COLUMN_ID = VideoResult.ID;
        public static final String COLUMN_KEY = "video_" + VideoResult.KEY;
        public static final String COLUMN_NAME = VideoResult.NAME;
        public static final String COLUMN_MOVIE_ID = VideoResult.MOVIE_ID;

        public static Uri buildVideosUri(int movie_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movie_id))
                    .build();
        }
    }

}
