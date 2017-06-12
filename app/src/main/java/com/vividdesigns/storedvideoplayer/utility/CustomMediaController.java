package com.vividdesigns.storedvideoplayer.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vividdesigns.storedvideoplayer.R;

/**
 * Extends MediaController class of Android to provide
 * Volume functionality
 */
public class CustomMediaController  extends MediaController {
    private Context mContext;


    public CustomMediaController(Context context) {
        super(context);
        mContext = context;

    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, AppConstants.FRAME_LENGTH);
        frameParams.gravity = Gravity.BOTTOM;
        frameParams.setMargins(0, AppConstants.FRAME_LENGTH, 0, 0);

        // Add Volume SeekBar to the layout
        View v = customSeekBarView();
        addView(v, frameParams);
    }

    /**
     * Inflates custom layout for the SeekBar and returns its view
     */
    private View customSeekBarView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View rootView = inflater.inflate(R.layout.custom_seekbar, null);

        SeekBar volumeSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        final TextView progressTextView = (TextView) rootView.findViewById(R.id.progressTextView);
        final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        volumeSeekBar.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
                double percentageProgress = (progress * 1.0/audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC)) * 100.0;
                int percentageInt = (int)percentageProgress;
                progressTextView.setVisibility(VISIBLE);
                progressTextView.setText(String.format("%d%%", percentageInt));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // empty implementation
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressTextView.setVisibility(INVISIBLE);
                // empty implementation
            }
        });
        return rootView;
    }



}
