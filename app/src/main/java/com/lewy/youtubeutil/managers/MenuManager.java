package com.lewy.youtubeutil.managers;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lewy.youtubeutil.R;
import com.lewy.youtubeutil.gui.NavigationDrawerItem;
import com.lewy.youtubeutil.gui.NavigationDrawerListAdapter;

import java.util.ArrayList;

/**
 * Created by dawid on 05.06.2016.
 */
public class MenuManager {

    private static NavigationDrawerListAdapter navigationDrawerListAdapter;
    protected static DrawerLayout mDrawerLayout;
    protected static ListView mDrawerList;

    private static ArrayList<NavigationDrawerItem> navigationDrawerItem;
    private static String[] navMenuTitles;

    public static void buildMenu(LinearLayout v, Context context) {
        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) v.findViewById(R.id.menu_list);

        navMenuTitles = context.getResources().getStringArray(R.array.nav_drawer_items);

        navigationDrawerItem = new ArrayList<>();
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[0]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[1]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[2]));
        navigationDrawerItem.add(new NavigationDrawerItem(navMenuTitles[3]));

        navigationDrawerListAdapter = new NavigationDrawerListAdapter(context, navigationDrawerItem);

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
}
