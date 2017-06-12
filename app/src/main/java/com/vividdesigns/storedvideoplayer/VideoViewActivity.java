package com.vividdesigns.storedvideoplayer;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vividdesigns.storedvideoplayer.dbhelper.VideoDataLoader;
import com.vividdesigns.storedvideoplayer.utility.AppConstants;
import com.vividdesigns.storedvideoplayer.utility.CustomMediaController;

public class VideoViewActivity extends AppCompatActivity {

    private int currentPosition;
    private int vidCount;
    private VideoView videoView;
    private ActionBar mSupportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        // Set Toolbar back arrow
        if(getSupportActionBar() != null) {
            mSupportActionBar = getSupportActionBar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        videoView = (VideoView)findViewById(R.id.videoView);

        // retrieve video positon and total count from cursor
        Bundle extras = getIntent().getExtras();
        currentPosition = extras.getInt(AppConstants.VID_POSITION);
        vidCount = extras.getInt(AppConstants.VID_COUNT);

        // set video path
        String fileName = VideoDataLoader.getVideoPathAtPosition(this, currentPosition);
        String videoTitle = VideoDataLoader.getVideoTitleFromPosition(this, currentPosition);
        if(mSupportActionBar != null) {
            mSupportActionBar.setTitle(videoTitle);
        }
        videoView.setVideoPath(fileName);

        // Set MediaController instance on the VideoView
        MediaController mediaController = new CustomMediaController(this);
        mediaController.setAnchorView(videoView);

        // Add support for previous/next button
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next button clicked
                if(currentPosition < vidCount-1) {
                    currentPosition++;
                } else {
                    currentPosition = 0;
                }
                String nextFileName = VideoDataLoader.getVideoPathAtPosition(VideoViewActivity.this, currentPosition);
                String videoTitle = VideoDataLoader.getVideoTitleFromPosition(VideoViewActivity.this, currentPosition);
                if(mSupportActionBar != null) {
                    mSupportActionBar.setTitle(videoTitle);
                }

                videoView.stopPlayback();
                videoView.setVideoPath(nextFileName);
                videoView.start();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //previous button clicked
                if(currentPosition > 0) {
                    currentPosition--;
                } else {
                    currentPosition = vidCount - 1;
                }
                String nextFileName = VideoDataLoader.getVideoPathAtPosition(VideoViewActivity.this, currentPosition);
                String videoTitle = VideoDataLoader.getVideoTitleFromPosition(VideoViewActivity.this, currentPosition);
                if(mSupportActionBar != null) {
                    mSupportActionBar.setTitle(videoTitle);
                }

                videoView.stopPlayback();
                videoView.setVideoPath(nextFileName);
                videoView.start();

            }
        });
        videoView.setMediaController(mediaController);


        videoView.requestFocus();
        // retrieve progress of video from bundle
        int progressOfVid = 0;
        if(savedInstanceState != null) {
            progressOfVid = savedInstanceState.getInt(AppConstants.PROGRESS_MICROSECONDS);
        }

        // set progress of video
        videoView.seekTo(progressOfVid);
        videoView.start();
    }

    /*
    save video progress for activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.PROGRESS_MICROSECONDS, videoView.getCurrentPosition());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
