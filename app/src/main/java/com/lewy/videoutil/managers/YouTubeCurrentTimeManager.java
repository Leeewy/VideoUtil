package com.lewy.videoutil.managers;

import android.os.AsyncTask;

import com.google.android.youtube.player.YouTubePlayer;
import com.lewy.videoutil.interfaces.CurrentTimeCallback;

/**
 * Created by lewy on 05.06.2016.
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
