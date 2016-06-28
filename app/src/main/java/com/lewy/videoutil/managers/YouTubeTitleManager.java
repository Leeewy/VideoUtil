package com.lewy.videoutil.managers;

import android.os.AsyncTask;
import android.util.Log;

import com.lewy.videoutil.interfaces.YouTubeTitleCallback;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by dawid on 05.06.2016.
 */
public class YouTubeTitleManager extends AsyncTask {

    private static final String TAG = "YouTubeTitleManager";

    private static final String YOU_TUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private YouTubeTitleCallback youTubeTitleCallback;

    private String youTubeUrl;

    public void setYouTubeTitleManager(YouTubeTitleCallback youTubeTitleCallback) {
        this.youTubeTitleCallback = youTubeTitleCallback;
    }

    public void setYouTubeUrl(String youTubeUrl) {
        this.youTubeUrl = youTubeUrl;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Log.i(TAG, "doInBackground()");

            if (youTubeUrl != null) {
                URL url = new URL("http://www.youtube.com/oembed?url=" + YOU_TUBE_BASE_URL + youTubeUrl + "&format=json");

                youTubeTitleCallback.title(new JSONObject(IOUtils.toString(url)).getString("title"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
