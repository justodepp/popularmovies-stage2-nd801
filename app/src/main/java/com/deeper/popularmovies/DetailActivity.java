package com.deeper.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deeper.popularmovies.adapter.MovieAdapter;
import com.deeper.popularmovies.adapter.ReviewAdapter;
import com.deeper.popularmovies.adapter.VideoAdapter;
import com.deeper.popularmovies.api.ApiEndPointHandler;
import com.deeper.popularmovies.api.ApiEndpointInterfaces;
import com.deeper.popularmovies.api.model.movieList.MovieListResponse;
import com.deeper.popularmovies.api.model.reviews.ReviewResponse;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;
import com.deeper.popularmovies.api.model.videos.VideoResponse;
import com.deeper.popularmovies.api.model.videos.VideoResult;
import com.deeper.popularmovies.utils.Params;
import com.deeper.popularmovies.api.model.movieList.MovieListResult;
import com.deeper.popularmovies.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener,
        ReviewAdapter.ReviewClickListener, VideoAdapter.VideoClickListener{

    private static MovieListResult mMovie;
    private Bundle extras;
    private boolean favourite = false;
    private FloatingActionButton fabFavourite;

    private RecyclerView rvReview;
    private ProgressBar pbReview;
    private TextView errorReview;
    private ArrayList<ReviewResult> reviewResults = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    private RecyclerView rvTrailer;
    private ProgressBar pbTrailer;
    private TextView errorTrailer;
    private ArrayList<VideoResult> videoResults = new ArrayList<>();
    private VideoAdapter videoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();

        extras = getIntent().getExtras();
        fabFavourite = findViewById(R.id.fab_favourite);
        fabFavourite.setOnClickListener(this);

        settingsToolbar();
        initView();

        initReviews();
        callReview();

        initVideo();
        callVideo();
    }

    private void initView() {
        ImageView poster = findViewById(R.id.posterImageView);
        TextView releaseDate = findViewById(R.id.releaseDate);
        TextView title = findViewById(R.id.title);
        TextView rating = findViewById(R.id.ratingTextView);
        RatingBar ratingBar = findViewById(R.id.rating);
        TextView overview = findViewById(R.id.overviewTextView);

        poster.setTransitionName(extras.getString(MainActivity.EXTRA_IMAGE_TRANSITION_NAME));

        fillData(poster, releaseDate, title, rating, ratingBar, overview);
    }

    private void fillData(ImageView poster, TextView releaseDate, TextView title, TextView rating,
                          RatingBar ratingBar, TextView overview) {
        loadBackdrop(poster, Params.IMAGE_PATH.concat(Params.IMAGE_SIZE[3])
                .concat(mMovie.getPosterPath()));

        releaseDate.setText(Utility.reformatDate(mMovie.getReleaseDate()));
        title.setText(mMovie.getTitle());
        rating.setText(mMovie.getVoteAverage() + "/10");
        ratingBar.setRating(mMovie.getVoteAverage() / 2f);
        overview.setText(mMovie.getOverview());
    }

    private void settingsToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        final ImageView backdrop = findViewById(R.id.backdrop);
        loadBackdrop(backdrop, Params.IMAGE_PATH.concat(Params.IMAGE_SIZE[3])
                .concat(mMovie.getBackdropPath()));

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mMovie.getOriginalTitle());
                    backdrop.setAlpha(.7f);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    backdrop.setAlpha(1f);
                    isShow = false;
                }
            }
        });
    }

    private void loadBackdrop(final ImageView backdrop, String path) {
        Picasso.with(this)
                .load(path)
                .into(backdrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        scheduleStartPostponedTransition(backdrop);
                        //supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    /**
     * @see "https://guides.codepath.com/android/shared-element-activity-transition"
     *
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     *
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     *     lives inside a Fragment hosted by the called Activity).
     *
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     *     asynchronously load/scale a bitmap before the transition can begin).
     **/
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    public static void setMovieDetails(MovieListResult movie){
        mMovie = movie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab_favourite){
            if(!favourite) {
                fabFavourite.setImageResource(R.drawable.ic_favourite_on);
                favourite = true;
                Snackbar.make(view, "Salvato tra i preferiti!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                fabFavourite.setImageResource(R.drawable.ic_favourite_off);
                favourite = false;
                Snackbar.make(view, "Eliminato dai preferiti!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private void showData(View rv, View pb, View error){
        rv.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    private void showError(View rv, View pb, View error){
        rv.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);    }

    private void initReviews() {
        rvReview = findViewById(R.id.rv_review);
        pbReview = findViewById(R.id.pb_review);
        errorReview = findViewById(R.id.tv_error_message_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        rvReview.setLayoutManager(layoutManager);
        rvReview.setHasFixedSize(true);
    }

    private void callReview(){
        pbReview.setVisibility(View.VISIBLE);

        ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(getApplicationContext());
        Call<ReviewResponse> responseReview = apiService.getReviews(String.valueOf(mMovie.getId()),
                ApiEndPointHandler.getDefaultParams(getApplicationContext()));

        responseReview.enqueue(new retrofit2.Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                if(response.isSuccessful()){
                    reviewResults.addAll(response.body().getResults());
                    reviewAdapter = new ReviewAdapter(reviewResults, DetailActivity.this);
                    rvReview.setAdapter(reviewAdapter);

                    showData(rvReview, pbReview, errorReview);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                showError(rvReview, pbReview, errorReview);
            }
        });
    }

    private void initVideo() {
        rvTrailer = findViewById(R.id.rv_trailer);
        pbTrailer = findViewById(R.id.pb_trailer);
        errorTrailer = findViewById(R.id.tv_error_message_trailer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        rvTrailer.setLayoutManager(layoutManager);
        rvTrailer.setHasFixedSize(true);
    }

    private void callVideo(){
        pbTrailer.setVisibility(View.VISIBLE);

        ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(getApplicationContext());
        Call<VideoResponse> responseTrailer = apiService.getVideos(String.valueOf(mMovie.getId()),
                ApiEndPointHandler.getDefaultParams(getApplicationContext()));

        responseTrailer.enqueue(new retrofit2.Callback<VideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideoResponse> call, @NonNull Response<VideoResponse> response) {
                if(response.isSuccessful()){
                    videoResults.addAll(response.body().getResults());
                    videoAdapter = new VideoAdapter(videoResults, DetailActivity.this);
                    rvTrailer.setAdapter(videoAdapter);

                    showData(rvTrailer, pbTrailer, errorTrailer);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoResponse> call, @NonNull Throwable t) {
                showError(rvTrailer, pbTrailer, errorTrailer);
            }
        });
    }

    @Override
    public void onClickReview(ReviewResult review) {

    }

    @Override
    public void onClickVideo(VideoResult video) {

    }
}
