package com.deeper.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deeper.popularmovies.R;
import com.deeper.popularmovies.api.model.reviews.ReviewResult;

import java.util.ArrayList;

/**
 * Created by justo on 24/02/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{
    private ArrayList<ReviewResult> reviews;

    private final ReviewAdapter.ReviewClickListener mReviewClickListener;

    public interface ReviewClickListener {
        void onClickReview(ReviewResult review);
    }

    public ReviewAdapter(ArrayList<ReviewResult> reviews, ReviewAdapter.ReviewClickListener reviewClickListener){
        this.reviews = reviews;
        mReviewClickListener = reviewClickListener;
    }

    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_item_list, parent, false);

        return new ReviewAdapter.ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() :  0;
    }

    class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvAuthor;
        final TextView tvContent;

        ReviewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);

            tvAuthor = itemView.findViewById(R.id.reviewAuthor);
            tvContent = itemView.findViewById(R.id.reviewContent);
        }

        public void bind(int position){
            tvAuthor.setText(reviews.get(position).getAuthor());
            tvContent.setText(reviews.get(position).getContent());
        }

        @Override
        public void onClick(View view) {
            mReviewClickListener.onClickReview(reviews.get(getAdapterPosition()));
        }
    }
}
