package com.lewy.youtubeutil.managers;

import android.os.AsyncTask;

import com.google.android.youtube.player.YouTubePlayer;
import com.lewy.youtubeutil.interfaces.CurrentTimeCallback;

/**
 * Created by dawid on 05.06.2016.
 */
public class YouTubeCurrentTimeManager extends AsyncTask {

    private CurrentTimeCallback currentTimeCallback;

    private YouTubePlayer youTubePlayer;

    public void setCurrentTimeCallback(CurrentTimeCallback currentTimeCallback) {
        this.currentTimeCallback = currentTimeCallback;
    }

    public void setYouTubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        while (true) {
            currentTimeCallback.receivedCurrentTime(youTubePlayer.getCurrentTimeMillis());
        }
    }
}