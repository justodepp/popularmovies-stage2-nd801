package com.deeper.popularmovies;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deeper.popularmovies.adapter.MovieAdapter;
import com.deeper.popularmovies.model.MovieList;
import com.deeper.popularmovies.utils.JsonUtils;
import com.deeper.popularmovies.utils.NetworkUtils;
import com.deeper.popularmovies.utils.Params;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieList>, MovieAdapter.MovieClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String queryMovie;
    private String nameSort;
    private int pageNum;

    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final String SEARCH_QUERY_SORT = "search";
    private static final String SEARCH_QUERY_PAGE = "page";

    private static final int MOVIEDB_SEARCH_LOADER = 23;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private MovieList movieList;
    private MovieAdapter movieAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queryMovie = "popular";
        pageNum = 1;

        mRecyclerView = findViewById(R.id.rv_main);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
        }

        /*
         * Initialize the loader
         */
        getSupportLoaderManager().initLoader(MOVIEDB_SEARCH_LOADER, null, this);

        makeMovieDbSearchQuery(queryMovie, pageNum);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                pageNum ++;
//                makeMovieDbSearchQuery(queryMovie, pageNum);
            }
        });
    }

    private void makeMovieDbSearchQuery(String queryMovie, int pageNum) {
        URL moviedbSearchUrl = NetworkUtils.buildUrl(queryMovie, pageNum);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, moviedbSearchUrl.toString());
        queryBundle.putString(SEARCH_QUERY_SORT, queryMovie);
        queryBundle.putInt(SEARCH_QUERY_PAGE, pageNum);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieDBLoader = loaderManager.getLoader(MOVIEDB_SEARCH_LOADER);
        if (movieDBLoader == null) {
            loaderManager.initLoader(MOVIEDB_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIEDB_SEARCH_LOADER, queryBundle, this);
        }
    }

    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.GONE);
        /* Then, make sure the JSON data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void errorNetwork() {
        mRecyclerView.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isOnline()) return false;
        if (Params.API_KEY.equals("")) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                queryMovie = "popular";
                makeMovieDbSearchQuery(queryMovie, pageNum);
                //new MovieFechtTask().execute(queryMovie);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.top_rated:
                queryMovie = "top_rated";
                makeMovieDbSearchQuery(queryMovie, pageNum);
                //new MovieFechtTask().execute(queryMovie);
                nameSort = "Top Rated Movies";
                setTitle(nameSort);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<MovieList> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieList>(this) {

            MovieList mMovieJson;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if (mMovieJson != null) {
                    deliverResult(mMovieJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public MovieList loadInBackground() {
                if (!isOnline()) {
                    errorNetwork();
                    return null;
                }
                if (Params.API_KEY.equals("")) {
                    errorNetwork();
                    mErrorMessageDisplay.setText(R.string.no_api_key);
                    return null;
                }
                URL movieUrl = NetworkUtils.buildUrl(queryMovie, pageNum);
                try {
                    String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    mMovieJson = JsonUtils.parseMovieJson(movieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mMovieJson;
            }

            @Override
            public void deliverResult(MovieList movieJson) {
                super.deliverResult(movieJson);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieList> loader, MovieList movieList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (movieList != null) {
            showJsonDataView();

            this.movieList = movieList;

            movieAdapter = new MovieAdapter(this, this.movieList.getResult(), this);
            mRecyclerView.setAdapter(movieAdapter);

        } else {
            showErrorMessage();
            Log.e(LOG_TAG, "Problems with adapter");
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {

    }

    @Override
    public void onClickMovie(int position) {
        if (!isOnline()) {
            errorNetwork();
            return;
        }

        Intent intent = new Intent(this, DetailActivity.class);

        DetailActivity.setMovieDetails(this.movieList.getResult().get(position));

        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onRefresh() {
        makeMovieDbSearchQuery(queryMovie, pageNum);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!isOnline()) return false;
        if (Params.API_KEY.equals("")) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular:
                queryMovie = "popular";
                makeMovieDbSearchQuery(queryMovie, pageNum);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.action_top_rated:
                queryMovie = "top_rated";
                makeMovieDbSearchQuery(queryMovie, pageNum);
                nameSort = "Top Rated Movies";
                setTitle(nameSort);
                break;
            case R.id.action_favorites:
                Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
                nameSort = "Favourite";
                setTitle(nameSort);
                break;
        }
        return true;
    }
}

