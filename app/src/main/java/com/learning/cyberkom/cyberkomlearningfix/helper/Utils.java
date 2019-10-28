package com.learning.cyberkom.cyberkomlearningfix.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.os.Build;

import java.util.HashMap;

public class Utils {

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
