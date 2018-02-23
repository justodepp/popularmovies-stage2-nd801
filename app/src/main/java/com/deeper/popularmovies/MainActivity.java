package com.deeper.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deeper.popularmovies.adapter.MovieAdapter;
import com.deeper.popularmovies.utils.Params;
import com.deeper.popularmovies.utils.api.ApiEndPointHandler;
import com.deeper.popularmovies.utils.api.ApiEndpointInterfaces;
import com.deeper.popularmovies.utils.api.model.movieList.MovieListResponse;
import com.deeper.popularmovies.utils.api.model.movieList.MovieListResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_TRANSITION_NAME = "transition_name";

    private String nameSort;
    private int pageNum;

    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final String SEARCH_QUERY_SORT = "search";
    private static final String SEARCH_QUERY_PAGE = "page";

    private static final int MOVIEDB_SEARCH_LOADER = 23;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private ArrayList<MovieListResult> movieListResults = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageNum = 1;

        mRecyclerView = findViewById(R.id.rv_main);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(R.id.appbar, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
        }

        if (!isOnline()) {
            errorNetwork();
        } else if (Params.API_KEY.equals("")) {
            errorNetwork();
            mErrorMessageDisplay.setText(R.string.no_api_key);
        } else {
            callPopular();
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        if (!isOnline()) {
            errorNetwork();
            return;
        }
        callPopular();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!isOnline()) return false;
        if (Params.API_KEY.equals("")) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular:
                callPopular();
                //makeMovieDbSearchQuery(queryMovie, pageNum);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.action_top_rated:
                callTopRated();
                //makeMovieDbSearchQuery(queryMovie, pageNum);
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

    @Override
    public void onClickMovie(int position, MovieListResult movie, ImageView clickedImage) {
        if (!isOnline()) {
            errorNetwork();
            return;
        }

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, DetailActivity.class);
        DetailActivity.setMovieDetails(movieListResults.get(position));
        intent.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(clickedImage));

        // Get the transition name from the string
        //String transitionName = getString(R.string.transition_string);
        // Define the view that the animation will start from
        //View viewStart = findViewById(R.id.image_poster);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        clickedImage,   // Starting view
                        ViewCompat.getTransitionName(clickedImage)    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    private void callPopular(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        movieListResults.clear();

        ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(getApplicationContext());
        Call<MovieListResponse> responsePopular = apiService.getPopular(ApiEndPointHandler.getDefaultParams(getApplicationContext()));

        responsePopular.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieListResponse> call, @NonNull Response<MovieListResponse> response) {
                if(response.isSuccessful()){
                    movieListResults.addAll(response.body().getResults());
                    movieAdapter = new MovieAdapter(MainActivity.this, movieListResults, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);

                    showJsonDataView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListResponse> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void callTopRated(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        movieListResults.clear();

        ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(getApplicationContext());
        Call<MovieListResponse> responseTopRated = apiService.getTopRated(ApiEndPointHandler.getDefaultParams(getApplicationContext()));

        responseTopRated.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieListResponse> call, @NonNull Response<MovieListResponse> response) {
                if(response.isSuccessful()){
                    movieListResults.addAll(response.body().getResults());
                    movieAdapter = new MovieAdapter(MainActivity.this, movieListResults, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);

                    showJsonDataView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListResponse> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }
}

