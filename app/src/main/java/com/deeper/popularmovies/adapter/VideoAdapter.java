package com.deeper.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deeper.popularmovies.R;
import com.deeper.popularmovies.api.model.videos.VideoResult;

import java.util.ArrayList;

/**
 * Created by justo on 24/02/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{
    private ArrayList<VideoResult> videos;

    private final VideoAdapter.VideoClickListener mVideoClickListener;

    public interface VideoClickListener {
        void onClickVideo(VideoResult video);
    }

    public VideoAdapter(ArrayList<VideoResult> videos, VideoAdapter.VideoClickListener videoClickListener){
        this.videos = videos;
        mVideoClickListener = videoClickListener;
    }

    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailer_item_list, parent, false);

        return new VideoAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() :  0;
    }

    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvLabel;

        VideoHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);

            tvLabel = itemView.findViewById(R.id.trailerLabel);
        }

        public void bind(int position){
            tvLabel.setText(videos.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            mVideoClickListener.onClickVideo(videos.get(getAdapterPosition()));
        }
    }
}
