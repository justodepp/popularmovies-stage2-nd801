package com.deeper.popularmovies.utils;

import com.deeper.popularmovies.model.Movie;
import com.deeper.popularmovies.model.MovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String MOVIE_PAGE = "page";
    private static final String MOVIE_RESULTS = "results";

    private static final String MOVIE_ID = "id";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_BACKDROP_PATH = "backdrop_path";
    private static final String MOVIE_RELEASE_DATE = "release_date";

    public static MovieList parseMovieJson(String json) {
        try {
            MovieList movieList = new MovieList();
            JSONObject root = new JSONObject(json);
            movieList.setPage(root.optInt(MOVIE_PAGE));
            JSONArray jsonArrayResult = root.getJSONArray(MOVIE_RESULTS);
            ArrayList<Movie> result = new ArrayList<>();
            for (int i = 0; i < jsonArrayResult.length(); i++) {
                Movie movie = new Movie();
                movie.setId(jsonArrayResult.getJSONObject(i).optInt(MOVIE_ID));
                movie.setRating(jsonArrayResult.getJSONObject(i).optString(MOVIE_RATING));
                movie.setTitle(jsonArrayResult.getJSONObject(i).optString(MOVIE_TITLE));
                movie.setPosterPath(jsonArrayResult.getJSONObject(i).optString(MOVIE_POSTER));
                movie.setOriginalTitle(jsonArrayResult.getJSONObject(i).optString(MOVIE_ORIGINAL_TITLE));
                movie.setOverview(jsonArrayResult.getJSONObject(i).optString(MOVIE_OVERVIEW));
                movie.setBackdropPath(jsonArrayResult.getJSONObject(i).optString(MOVIE_BACKDROP_PATH));
                movie.setReleaseDate(jsonArrayResult.getJSONObject(i).optString(MOVIE_RELEASE_DATE));
                result.add(movie);
            }
            movieList.setResult(result);
            return movieList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
