package com.vividdesigns.storedvideoplayer.dbhelper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class VideoDataLoader {

    /**
     * @param context provided
     * @return Cursor for video info
     */
    public static Cursor getVidCursor(Context context) {

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DURATION
        };

        return context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );

    }


    /**
     * Returns path string of video for position from cursor
     */
    public static String getVideoPathAtPosition(Context context, int position) {
        Cursor vidCursor = getVidCursor(context);
        vidCursor.moveToPosition(position);
        int dataIndex = vidCursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        String filename = vidCursor.getString(dataIndex);
        vidCursor.close();
        return filename;
    }


    /**
     * Returns video title for position from cursor
     */
    public static String getVideoTitleFromPosition(Context context, int position) {
        Cursor vidCursor = getVidCursor(context);
        vidCursor.moveToPosition(position);
        int displayNameIndex = vidCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
        String displayName = vidCursor.getString(displayNameIndex);
        vidCursor.close();
        return displayName;
    }

    /**
     * Obtains thumbnail of video from its id
     */
    public static String getVideoThumbnail(Context context, long videoID) {
        Cursor cursor = null;
        try {
            String[] projection = {
                    MediaStore.Video.Thumbnails.DATA,
            };
            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                    new String[]{String.valueOf(videoID)},
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }




}
