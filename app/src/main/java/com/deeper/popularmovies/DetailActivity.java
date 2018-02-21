package com.deeper.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deeper.popularmovies.model.Movie;
import com.deeper.popularmovies.utils.Params;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private static Movie mMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        settingsToolbar();
        fillData();
    }

    private void fillData() {
        ImageView poster = findViewById(R.id.posterImageView);
        loadBackdrop(poster, Params.IMAGE_PATH.concat(Params.IMAGE_SIZE[3])
                .concat(mMovie.getPosterPath()));

        TextView releaseDate = findViewById(R.id.releaseDate);
        releaseDate.setText(reformatDate(mMovie.getReleaseDate()));

        TextView title = findViewById(R.id.title);
        title.setText(mMovie.getTitle());

        TextView rating = findViewById(R.id.ratingTextView);
        rating.setText(mMovie.getRating()+ "/10");

        RatingBar ratingBar = findViewById(R.id.rating);
        ratingBar.setRating(Float.valueOf(mMovie.getRating()) / 2f);

        TextView overview = findViewById(R.id.overviewTextView);
        overview.setText(mMovie.getOverview());
    }

    private String reformatDate(String releaseDate) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
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

    private void loadBackdrop(ImageView backdrop, String path) {
        Picasso.with(this)
                .load(path)
                .into(backdrop);
    }

    public static void setMovieDetails(Movie movie){
        mMovie = movie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
