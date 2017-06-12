package com.vividdesigns.storedvideoplayer.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vividdesigns.storedvideoplayer.R;
import com.vividdesigns.storedvideoplayer.dbhelper.VideoDataLoader;
import com.vividdesigns.storedvideoplayer.utility.ConversionUtil;

import java.util.Locale;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public VideoListAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Cursor cursor = null;
        try {
            cursor = VideoDataLoader.getVidCursor(mContext);
            cursor.moveToPosition(position);
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;

            long videoId = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Video.Media._ID));

            String path = VideoDataLoader.getVideoThumbnail(mContext, videoId);
            Picasso.with(mContext)
                    .load("file://" + path)
                    .placeholder(R.drawable.default_video)
                    .into(videoViewHolder.posterImageView);

            int displayNameIndex = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
            String displayName = cursor.getString(displayNameIndex);
            videoViewHolder.titleTextView.setText(displayName);

            int durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
            long duration = cursor.getLong(durationIndex);
            String durationString = ConversionUtil.getDurationString(duration, Locale.getDefault());
            videoViewHolder.durationTextView.setText(durationString);

            int sizeIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
            long size = cursor.getLong(sizeIndex);
            String sizeString = ConversionUtil.humanReadableByteCount(size, true, Locale.getDefault());
            videoViewHolder.sizeTextView.setText(sizeString);

            int dateIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
            long timeStamp = cursor.getLong(dateIndex);
            String formattedDateString = ConversionUtil.getDateString(timeStamp, Locale.getDefault());
            videoViewHolder.dateTextView.setText(formattedDateString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    @Override
    public int getItemCount() {
        Cursor vidCursor = VideoDataLoader.getVidCursor(mContext);
        int count = vidCursor.getCount();
        vidCursor.close();
        return count;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public TextView titleTextView;
        public TextView durationTextView;
        public TextView sizeTextView;
        public TextView dateTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.poster);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            durationTextView = (TextView) itemView.findViewById(R.id.duration);
            sizeTextView = (TextView) itemView.findViewById(R.id.size);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
        }
    }

}
