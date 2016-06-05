package com.lewy.youtubeutil.managers;

import android.os.AsyncTask;
import android.util.Log;

import com.lewy.youtubeutil.interfaces.YouTubeControllerCallback;

/**
 * Created by dawid on 05.06.2016.
 */
public class YouTubeControllersTimeManager extends AsyncTask {

    private static final int TIME = 3000;

    private YouTubeControllerCallback youTubeControllerCallback;

    public void setYouTubeControllerCallback(YouTubeControllerCallback youTubeControllerCallback) {
        this.youTubeControllerCallback = youTubeControllerCallback;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("YouTubeTimeManager", "doInBackground()");

        try {
            Thread.sleep(TIME);

            youTubeControllerCallback.hideControllers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
