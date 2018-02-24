package com.deeper.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.deeper.popularmovies.R;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;

import static android.view.Window.FEATURE_NO_TITLE;

/**
 * Created by justo on 24/02/18.
 */

public class ReviewDialog extends DialogFragment {
    private ReviewResult review;

    public ReviewDialog setReview(ReviewResult review) {
        this.review = review;
        return this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_review, container, false);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvAuthor = view.findViewById(R.id.reviewAuthor);
        TextView tvContent = view.findViewById(R.id.reviewContent);

        getDialog().requestWindowFeature(FEATURE_NO_TITLE);

        tvContent.setText(review.getContent());
        tvAuthor.setText(review.getAuthor());
    }
}
