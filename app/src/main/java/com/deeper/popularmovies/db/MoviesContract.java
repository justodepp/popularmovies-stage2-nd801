package com.deeper.popularmovies.db;

import android.net.Uri;

import com.deeper.popularmovies.api.model.movieList.MovieListResult;
import com.deeper.popularmovies.api.model.reviews.ReviewResponse;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;
import com.deeper.popularmovies.api.model.videos.VideoResponse;
import com.deeper.popularmovies.api.model.videos.VideoResult;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.deeper.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

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
        public static final String COLUMN_MOVIE_ID = ReviewResponse.MOVIE_ID;

        public static Uri buildMovieReviewsUri(int movie_id) {
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
        public static final String COLUMN_MOVIE_ID = VideoResponse.MOVIE_ID;

        public static Uri buildMovieVideosUri(int movie_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movie_id))
                    .build();
        }
    }

}
