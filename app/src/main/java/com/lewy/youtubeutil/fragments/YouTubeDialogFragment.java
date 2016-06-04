package com.lewy.youtubeutil.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
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
import com.lewy.youtubeutil.managers.TimeCalculator;

import java.util.ArrayList;

/**
 * Created by dawid on 15.05.2016.
 */
public class YouTubeDialogFragment extends DialogFragment {

    private static final String TAG = "YouTubeDialogFragment";

    private NavigationDrawerListAdapter navigationDrawerListAdapter;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;

    private ArrayList<NavigationDrawerItem> navigationDrawerItem;
    private String[] navMenuTitles;

    private ImageView playStopViewButton;
    private ImageView playStopSliderButton;

    private TextView currentTimeView;
    private TextView maxTimeView;

    private SeekBar seekBar;

    private TextView videoTitle;

    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private YouTubePlayer youTubePlayer;

    private boolean playing;

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

        buildMenu(v);
        initAllViews(v);

        return v;
    }

    private void buildMenu(LinearLayout v) {
        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) v.findViewById(R.id.menu_list);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navigationDrawerItem = new ArrayList<>();
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[0]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[1]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[2]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[3]));

        navigationDrawerListAdapter = new NavigationDrawerListAdapter(getContext(), navigationDrawerItem);

        mDrawerList.setAdapter(navigationDrawerListAdapter);
        mDrawerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mDrawerLayout.closeDrawers();
                    }
                }
        );
    }

    private void initAllViews(LinearLayout v) {
        playStopViewButton = (ImageView) v.findViewById(R.id.play_stop_view_button);
        playStopViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayStopButtons();
            }
        });

        playStopSliderButton = (ImageView)  v.findViewById(R.id.play_stop_slider_button);
        playStopSliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayStopButtons();
            }
        });

        currentTimeView = (TextView) v.findViewById(R.id.current_time);
        maxTimeView = (TextView) v.findViewById(R.id.max_time);

        seekBar = (SeekBar) v.findViewById(R.id.seek_bar);

        videoTitle = (TextView) v.findViewById(R.id.video_title);
    }

    private void changePlayStopButtons() {
        if(playing) {
            playStopViewButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_play_circle_outline));
            playStopSliderButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_play_circle_fill));
            youTubePlayer.pause();
        } else {
            playStopViewButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_pause_circle_outline));
            playStopSliderButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_av_pause_circle_fill));
            youTubePlayer.play();
        }

        playing = !playing;
    }

    private void setMaxTime() {
        maxTimeView.setText(TimeCalculator.secondsToString(youTubePlayer.getDurationMillis() / 1000));
    }

    private void setVideoTitle(String s) {
        videoTitle.setText(s);
        videoTitle.setVisibility(View.VISIBLE);
    }

    private void hideYouTubeLoader(boolean isBuffering) {
        ViewGroup ytView = (ViewGroup)youTubePlayerSupportFragment.getView();
        ProgressBar progressBar;
        try {
            // As of 2016-02-16, the ProgressBar is at position 0 -> 3 -> 2 in the view tree of the Youtube Player Fragment
            ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
            ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
            progressBar = (ProgressBar)child2.getChildAt(2);
        } catch (Throwable t) {
            // As its position may change, we fallback to looking for it
            progressBar = findProgressBar(ytView);
            // TODO I recommend reporting this problem so that you can update the code in the try branch: direct access is more efficient than searching for it
        }

        int visibility = isBuffering ? View.VISIBLE : View.INVISIBLE;
        if (progressBar != null) {
            progressBar.setVisibility(visibility);
            // Note that you could store the ProgressBar instance somewhere from here, and use that later instead of accessing it again.
        }
    }

    private ProgressBar findProgressBar(View view) {
        if (view instanceof ProgressBar) {
            return (ProgressBar)view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {}

        @Override
        public void onLoaded(String s) {
            setVideoTitle(s);
        }

        @Override
        public void onAdStarted() {}

        @Override
        public void onVideoStarted() {
            changePlayStopButtons();
            setMaxTime();
        }

        @Override
        public void onVideoEnded() {
            changePlayStopButtons();
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
}
