package com.av.chatz.fragments;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ankit on 06-02-2018.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            RequestsFragment rf = new RequestsFragment();
            return rf;
        } else if (position == 1) {
            ChatsFragment cf = new ChatsFragment();
            return cf;
        } else {
            FriendsFragment ff = new FriendsFragment();
            return ff;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Requests";
        } else if (position == 1) {
            return "Chats";
        } else {
            return "Friends";
        }
    }

}
