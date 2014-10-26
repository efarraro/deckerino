package com.slothwerks.hearthstone.compendiumforhearthstone.ui.navigation;

/**
 * Created by Eric on 9/17/2014.
 */
public class NavDrawerItem {

    protected String mTitle;
    protected NavDrawerItemType mItemType;

    public NavDrawerItem(String title, NavDrawerItemType itemType)
    {
        mTitle = title;
        mItemType = itemType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public NavDrawerItemType getItemType() {
        return mItemType;
    }

    public void setItemType(NavDrawerItemType itemType) {
        mItemType = itemType;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
