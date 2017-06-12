package com.vividdesigns.storedvideoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vividdesigns.storedvideoplayer.adapter.RecyclerTouchListener;
import com.vividdesigns.storedvideoplayer.adapter.VideoListAdapter;
import com.vividdesigns.storedvideoplayer.dbhelper.VideoDataLoader;
import com.vividdesigns.storedvideoplayer.utility.AppConstants;


public class MainActivity extends AppCompatActivity {

    private TextView errorTextView;
    private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        mFrameLayout = (FrameLayout) findViewById(R.id.sampleContentLayout);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        } else {
            initializeUI();
        }

    }

    /**
     * Requests the READ_EXTERNAL_STORAGE permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mFrameLayout, R.string.permission_storage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    AppConstants.READ_STORAGE_PERMISSION);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    AppConstants.READ_STORAGE_PERMISSION);
        }
    }


    /**
     * Initializes UserInterface in case of permission if granted
     */
    private void initializeUI() {
        Cursor cursor = VideoDataLoader.getVidCursor(this);
        final int totalVidCount = cursor.getCount();
        if(totalVidCount == 0) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(R.string.no_videos);
            return;
        }

        // Set Adapter and recycler view
        VideoListAdapter videoListAdapter = new VideoListAdapter(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(MainActivity.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(videoListAdapter);

        // Item listener to open VideoActivity through intent
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,
                        VideoViewActivity.class);
                intent.putExtra(AppConstants.VID_POSITION, position);
                intent.putExtra(AppConstants.VID_COUNT, totalVidCount);
                startActivity(intent);
            }
        }));

    }

    /**
     * Checks for permission action of user action on permission.
     * Show errorText if permission denied. Else show RecyclerView.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    initializeUI();

                } else {
                    // permission denied
                    // show error text
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Interface for RecyclerView to provide onClickItem functionality
     */
    public interface ClickListener {
        void onClick(View view, int position);
    }

}
