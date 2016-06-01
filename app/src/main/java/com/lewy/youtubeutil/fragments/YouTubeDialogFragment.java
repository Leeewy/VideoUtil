package com.lewy.youtubeutil.fragments;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lewy.youtubeutil.R;
import com.lewy.youtubeutil.gui.NavigationDrawerItem;
import com.lewy.youtubeutil.gui.NavigationDrawerListAdapter;

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

    public static YouTubeDialogFragment newInstance(){
        YouTubeDialogFragment youTubeDialogFragment = new YouTubeDialogFragment();
        return youTubeDialogFragment;
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

        return v;
    }
}
