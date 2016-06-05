package com.lewy.youtubeutil.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.lewy.youtubeutil.R;
import com.lewy.youtubeutil.gui.NavigationDrawerItem;
import com.lewy.youtubeutil.gui.NavigationDrawerListAdapter;
import com.lewy.youtubeutil.interfaces.CurrentTimeCallback;
import com.lewy.youtubeutil.interfaces.YouTubeControllerCallback;
import com.lewy.youtubeutil.interfaces.YouTubeTitleCallback;
import com.lewy.youtubeutil.managers.MenuManager;
import com.lewy.youtubeutil.managers.TimeCalculator;
import com.lewy.youtubeutil.managers.YouTubeControllersTimeManager;
import com.lewy.youtubeutil.managers.YouTubeCurrentTimeManager;
import com.lewy.youtubeutil.managers.YouTubeTitleManager;

import java.util.ArrayList;

/**
 * Created by dawid on 15.05.2016.
 */
public class YouTubeDialogFragment extends DialogFragment implements CurrentTimeCallback, YouTubeControllerCallback, YouTubeTitleCallback {

    private static final String TAG = "YouTubeDialogFragment";

    private static final String YOU_TUBE_ID = "H-IVzFIRSVE";

    private static final int MILISECONDS = 1000;

    private ImageView playStopViewButton;

    private TextView currentTimeView;
    private TextView maxTimeView;

    private SeekBar seekBar;

    private TextView videoTitle;

    private LinearLayout youTubeControllersLayout;

    private RelativeLayout youTubeControllers;

    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private YouTubePlayer youTubePlayer;

    private AsyncTask youTubeCurrentTimeManager;

    private YouTubeControllersTimeManager youTubeControllersTimeManager;

    private boolean playing;

    private boolean trackingTouch;

    private int maxSize;

    public static YouTubeDialogFragment newInstance(){
        YouTubeDialogFragment youTubeDialogFragment = new YouTubeDialogFragment();
        return youTubeDialogFragment;
    }

    public void setYouTubePlayerSupportFragment(YouTubePlayerSupportFragment youTubePlayerSupportFragment) {
        this.youTubePlayerSupportFragment = youTubePlayerSupportFragment;
    }

    public void setYouTubePlayer(YouTubePlayer youTubePlayer) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        youTubePlayer.loadVideo(YOU_TUBE_ID);

        this.youTubePlayer = youTubePlayer;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setDimAmount(0);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout v = (LinearLayout) inflater.inflate(R.layout.you_tube_dialog_fragment, container, true);

        MenuManager.buildMenu(v, getContext());
        initAllViews(v);

        return v;
    }

    private void initAllViews(LinearLayout v) {
        playStopViewButton = (ImageView) v.findViewById(R.id.play_stop_view_button);
        playStopViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayStopButtons();
            }
        });

        currentTimeView = (TextView) v.findViewById(R.id.current_time);
        maxTimeView = (TextView) v.findViewById(R.id.max_time);

        seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(trackingTouch) {
                    youTubePlayer.seekToMillis(progress * MILISECONDS);
                    startYouTubeControllersTimeManager();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                trackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                trackingTouch = false;
            }
        });

        videoTitle = (TextView) v.findViewById(R.id.video_title);

        youTubeControllersLayout = (LinearLayout) v.findViewById(R.id.you_tube_controllers_layout);

        youTubeControllers = (RelativeLayout) v.findViewById(R.id.you_tube_controllers);
        youTubeControllers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showControllers();
                startYouTubeControllersTimeManager();
            }
        });
    }

    private void changePlayStopButtons() {
        if(playing) {
            playStopViewButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_play_circle_outline));
            youTubePlayer.pause();

            stopYouTubeControllersTimeManager();
        } else {
            playStopViewButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_pause_circle_outline));
            youTubePlayer.play();

            if(videoTitle.getVisibility() == View.GONE) {
                getVideoTitle(YOU_TUBE_ID);
            }

            startYouTubeControllersTimeManager();
        }

        playing = !playing;
    }

    private void setMaxTime() {
        maxSize = youTubePlayer.getDurationMillis() / MILISECONDS;
        maxTimeView.setText(TimeCalculator.secondsToString(maxSize));
        seekBar.setMax(maxSize);
    }

    private void setVideoTitle(final String title) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videoTitle.setText(title);
                    videoTitle.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void hideVideoTitle() {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videoTitle.setText("");
                    videoTitle.setVisibility(View.GONE);
                }
            });
        }
    }

    private void hideYouTubeLoader(boolean isBuffering) {
        ViewGroup ytView = (ViewGroup)youTubePlayerSupportFragment.getView();
        ProgressBar progressBar;

        try {
            ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
            ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
            progressBar = (ProgressBar)child2.getChildAt(2);
        } catch (Throwable t) {
            progressBar = findProgressBar(ytView);
        }

        int visibility = isBuffering ? View.VISIBLE : View.INVISIBLE;
        if (progressBar != null) {
            progressBar.setVisibility(visibility);
        }
    }

    private ProgressBar findProgressBar(View view) {
        if (view instanceof ProgressBar) {
            return (ProgressBar) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    private void startGettingCurrentTime() {
        YouTubeCurrentTimeManager youTubeCurrentTimeManager = new YouTubeCurrentTimeManager();
        youTubeCurrentTimeManager.setYouTubePlayer(youTubePlayer);
        youTubeCurrentTimeManager.setCurrentTimeCallback(this);
        this.youTubeCurrentTimeManager = youTubeCurrentTimeManager.execute();
    }

    private void stopGettingCurrentTime() {
        if(youTubeCurrentTimeManager != null) {
            youTubeCurrentTimeManager.cancel(true);
        }
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {}

        @Override
        public void onLoaded(String s) {}

        @Override
        public void onAdStarted() {}

        @Override
        public void onVideoStarted() {
            changePlayStopButtons();
            setMaxTime();
            startGettingCurrentTime();

            startYouTubeControllersTimeManager();
            getVideoTitle(YOU_TUBE_ID);
        }

        @Override
        public void onVideoEnded() {
            stopGettingCurrentTime();
            changePlayStopButtons();

            youTubeControllersLayout.setVisibility(View.VISIBLE);
            playStopViewButton.setVisibility(View.VISIBLE);

            stopYouTubeControllersTimeManager();

            hideVideoTitle();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {}
    };

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {}

        @Override
        public void onPaused() {}

        @Override
        public void onStopped() {}

        @Override
        public void onBuffering(boolean b) {
            hideYouTubeLoader(b);
        }

        @Override
        public void onSeekTo(int i) {}
    };

    @Override
    public void receivedCurrentTime(final int currentTime) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int current = currentTime / MILISECONDS;
                    currentTimeView.setText(TimeCalculator.secondsToString(current));

                    if (!trackingTouch) {
                        seekBar.setProgress(current);
                    }
                }
            });
        }
    }

    @Override
    public void hideControllers() {
        Log.i(TAG, "hideControllers()");
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    youTubeControllersLayout.setVisibility(View.INVISIBLE);
                    playStopViewButton.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void showControllers() {
        Log.i(TAG, "showControllers()");

        youTubeControllersLayout.setVisibility(View.VISIBLE);
        playStopViewButton.setVisibility(View.VISIBLE);
    }

    private void stopYouTubeControllersTimeManager() {
        if(youTubeControllersTimeManager != null) {
            youTubeControllersTimeManager.cancel(true);
        }
    }

    private void startYouTubeControllersTimeManager() {
        stopYouTubeControllersTimeManager();

        youTubeControllersTimeManager = new YouTubeControllersTimeManager();
        youTubeControllersTimeManager.setYouTubeControllerCallback(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            youTubeControllersTimeManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            youTubeControllersTimeManager.execute();
        }
    }

    private void getVideoTitle(String id) {
        YouTubeTitleManager youTubeTitleManager = new YouTubeTitleManager();
        youTubeTitleManager.setYouTubeTitleManager(this);
        youTubeTitleManager.setYouTubeUrl(id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            youTubeTitleManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            youTubeTitleManager.execute();
        }
    }

    @Override
    public void title(String title) {
        setVideoTitle(title);
    }
}
