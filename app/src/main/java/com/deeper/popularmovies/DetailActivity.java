package com.deeper.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deeper.popularmovies.utils.Params;
import com.deeper.popularmovies.utils.api.model.movieList.MovieListResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private static MovieListResult mMovie;
    private Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();

        extras = getIntent().getExtras();

        settingsToolbar();
        initView();
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

        releaseDate.setText(reformatDate(mMovie.getReleaseDate()));
        title.setText(mMovie.getTitle());
        rating.setText(mMovie.getVoteAverage() + "/10");
        ratingBar.setRating(mMovie.getVoteAverage() / 2f);
        overview.setText(mMovie.getOverview());
    }

    private String reformatDate(String releaseDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        Date newDate = null;
        try {
            newDate = format.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("EEE, d MMM yyyy");
        return format.format(newDate);
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
}
