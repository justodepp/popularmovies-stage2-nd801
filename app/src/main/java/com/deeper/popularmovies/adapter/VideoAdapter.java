package com.deeper.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deeper.popularmovies.DetailActivity;
import com.deeper.popularmovies.R;
import com.deeper.popularmovies.api.model.videos.VideoResult;
import com.deeper.popularmovies.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by justo on 24/02/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{

    private Context context;
    private ArrayList<VideoResult> videos;

    private final VideoAdapter.VideoClickListener mVideoClickListener;

    public interface VideoClickListener {
        void onClickVideo(VideoResult video);
    }

    public VideoAdapter(Context context, ArrayList<VideoResult> videos, VideoAdapter.VideoClickListener videoClickListener){
        this.context = context;
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

    private void loadImage(final ImageView image, Uri path) {
        Picasso.with(context)
                .load(path)
                .into(image);
    }

    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvLabel;
        ImageView imageView;

        VideoHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);

            tvLabel = itemView.findViewById(R.id.trailerLabel);
            imageView = itemView.findViewById(R.id.trailerImage);
        }

        public void bind(int position) {
            tvLabel.setText(videos.get(position).getName());
            loadImage(imageView, Utility.getYoutubeThumb(videos.get(position).getKey(),
                    Utility.IMAGE_QUALITY[2]));
        }
        @Override
        public void onClick(View view) {
            mVideoClickListener.onClickVideo(videos.get(getAdapterPosition()));
        }
    }
}
